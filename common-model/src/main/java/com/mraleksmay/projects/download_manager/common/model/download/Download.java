package com.mraleksmay.projects.download_manager.common.model.download;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.Restoreable;
import com.mraleksmay.projects.download_manager.common.model.category.Category;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

/**
 * Contains main information about download.
 */
public abstract class Download implements Serializable, Restoreable {
    /**
     * The state in which the download may be
     */
    public enum Status {
        CREATED,
        PAUSED,
        UPDATING,
        DOWNLOADING,
        DOWNLOADED,
        FINISHING,
        STOP,
        ERROR
    }

    /**
     * Unique download identifier used during serialization.
     */
    @Expose
    private String serializableId = "null";
    /**
     * Global download id
     */
    private static long globalId = 0;
    /**
     * Download id
     */
    private long id;
    /**
     * Download file name.
     */
    @Expose
    @NotNull
    private String fileName;
    /**
     * File extension.
     */
    @Expose
    @NotNull
    private String extension;
    /**
     * Total size.
     */
    @Expose
    private long fullSize;
    /**
     * Now loaded bytes.
     */
    @Expose
    private long currentSize;
    /**
     * A folder for temporary storage of data during download.
     */
    @Expose
    @NotNull
    private String tmpDirStr;
    /**
     * The downloaded file will be moved to this directory.
     */
    @Expose
    @NotNull
    private String outputDirStr;
    /**
     * Download url.
     */
    @Expose
    @NotNull
    private URL url;
    /**
     * User authentication information.
     */
    @Expose
    private AuthenticationData authData;
    /**
     * The class of a specific implementation of this authorizationData. Used when deserializing.
     */
    @Expose
    private String authorizationDataClassStr = "null";
    /**
     * Current download status.
     */
    private volatile Status status = Status.CREATED;
    /**
     * Download category.
     */
    @Expose
    @NotNull
    private Category category;
    /**
     * Used to display download information as desired.
     */
    @NotNull
    private DownloadFormatter formatter;
    /**
     * The class of a specific implementation of this download formatter. Used when deserializing.
     */
    @Expose
    private String formatterClassStr = "null";
    /**
     * Time when the download was added.
     */
    @Expose
    private long addTime;
    /**
     * The class of a specific implementation of this download. Used when deserializing.
     */
    @Expose
    private String classStr;


    {
        setClazz(this.getClass());
    }

    // Constructors
    public Download() {
        setId(globalId++);
    }

    public Download(Download download) throws IOException {
        this(download.getId(),
                download.getFileName(),
                download.getUrl(),
                download.getOutputDir(),
                download.getExtension(),
                download.getAuthData(),
                download.getCategory(),
                download.getFullSize());
    }

    public Download(@NotNull final String fileName,
                    @NotNull final URL url,
                    @NotNull final File outputDir,
                    @NotNull final String extension,
                    AuthenticationData authData,
                    @NotNull final Category category,
                    long fullSize) throws IOException {
        this(globalId++, fileName, url, outputDir, extension, authData, category, fullSize);
    }

    private Download(long id,
                     @NotNull final String fileName,
                     @NotNull final URL url,
                     @NotNull final File outputDir,
                     @NotNull final String extension,
                     AuthenticationData authData,
                     @NotNull final Category category,
                     long fullSize) throws IOException {
        String sid = String.format("%s-%s-%s-%s",
                System.currentTimeMillis(),
                System.nanoTime(),
                new Random().nextInt(),
                new Random().nextLong());
        setSerializableId(sid);

        setId(id);
        setFileName(fileName.trim());
        setExtension(extension);
        setUrl(url);
        setTmpDir(category.getTmpDir());
        setOutputDir(outputDir);
        setAuthData(authData);
        setCategory(category);
        setFullSize(fullSize);
        setTime(System.currentTimeMillis());
    }

    // Methods
    public void addBytes(int downloadedBytes) {
        currentSize += downloadedBytes;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deserialize(Object obj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Download download = this;
        Map<String, Object> dmap = (Map<String, Object>) obj;

        download.setSerializableId((String) dmap.get("serializableId"));
        download.setFileName((String) dmap.get("fileName"));
        download.setFullSize((long) Double.parseDouble(dmap.get("fullSize") + ""));
        download.setCurrentSize((long) Double.parseDouble(dmap.get("currentSize") + ""));

        String url = (String) dmap.get("url");
        String outputDirStr = (String) dmap.get("outputDirStr");
        String tmpDirStr = (String) dmap.get("tmpDirStr");
        String authorizationDataClassStr = (String) dmap.get("authorizationDataClassStr");
        String formatterClassStr = (String) dmap.get("formatterClassStr");
        Map<String, Object> cmap = (Map<String, Object>) dmap.get("category");

        download.setUrl(new URL(url));
        download.setOutputDir(new File(outputDirStr));
        download.setTmpDir(new File(tmpDirStr));
        this.setStatus(Download.Status.PAUSED);

        if (!"null".equals(authorizationDataClassStr)) {
            Map<String, Object> authDataObj = (Map<String, Object>) dmap.get("authData");

            if (authDataObj != null) {
                AuthenticationData authData = (AuthenticationData) Class.forName(authorizationDataClassStr).newInstance();

                this.setAuthData(authData);
            }
        }

        if (!"null".equals(formatterClassStr)) {
            DownloadFormatter downloadFormatter = (DownloadFormatter) Class.forName(formatterClassStr).newInstance();
            downloadFormatter.setDownload(download);
            download.setFormatter(downloadFormatter);
        }
    }

    // Getters and Setters
    public String getSerializableId() {
        return serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public static void setGlobalId(long globalId) {
        Download.globalId = globalId;
    }

    public static long getGlobalId() {
        return globalId;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static String suggestNameFrom(@NotNull String url) {
        String name = "";
        String urlStr = url + "";
        urlStr = urlStr.trim();

        if (urlStr.charAt(url.length() - 1) == '/') {
            urlStr = urlStr.substring(0, urlStr.length() - 1);
        }

        int lIndex = urlStr.lastIndexOf('/');

        if (lIndex >= 0) {
            name = urlStr.substring(lIndex + 1);
        } else {
            name = new Random().nextInt() + "";
        }

        return name.trim();
    }

    public static String getFileName(String fullName) {
        String fileName = fullName.replaceAll("[^\\w\\d\\.\\- ]", "");

        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getFullSize() {
        return fullSize;
    }

    public void setFullSize(long fullSize) {
        this.fullSize = fullSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public File getTmpDir() {
        return new File(tmpDirStr);
    }

    public void setTmpDir(@NotNull final File tmpDir) throws IOException {
        this.tmpDirStr = tmpDir.getCanonicalPath();
    }

    public File getOutputDir() throws IOException {
        return new File(outputDirStr);
    }

    public void setOutputDir(@NotNull final File outputDir) throws IOException {
        this.outputDirStr = outputDir.getCanonicalPath();
    }

    public File getFullTempPathToFile() throws IOException {
        return new File(getTmpDir() + "/" + fileName).getCanonicalFile();
    }

    public File getFullPathToFile() throws IOException {
        String fullPath = getOutputDir() + "/" + getFileName().trim() + "." + getExtension();
        return new File(fullPath).getCanonicalFile();
    }

    public URL getUrl() throws MalformedURLException {
        return new URL(url.toString());
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public float getProgress() {
        float progress = ((float) currentSize / (float) fullSize) * 100f;

        if (Float.isNaN(progress))
            return 0f;
        else
            return Math.abs(progress);
    }

    public AuthenticationData getAuthData() {
        return authData;
    }

    protected void setAuthData(AuthenticationData authData) {
        if (authData != null) {
            this.authData = authData;
            this.setAuthorizationDataClassStr(authData.getClass());
        }
    }

    public Class<? extends AuthenticationData> getAuthorizationDataClass() throws ClassNotFoundException {
        return (Class<AuthenticationData>) Class.forName(authorizationDataClassStr);
    }

    public void setAuthorizationDataClassStr(Class<? extends AuthenticationData> authorizationDataClass) {
        this.authorizationDataClassStr = authorizationDataClass.getCanonicalName();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull final Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull final Category category) {
        this.category = category;
    }

    public DownloadFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DownloadFormatter formatter) {
        formatter.setDownload(this);
        this.formatter = formatter;
        this.setFormatterClass(formatter.getClass());
    }

    public Class<? extends DownloadFormatter> getFormatterClass() throws ClassNotFoundException {
        return (Class<? extends DownloadFormatter>) Class.forName(formatterClassStr);
    }

    public void setFormatterClass(Class<? extends DownloadFormatter> formatterClass) {
        this.formatterClassStr = formatterClass.getCanonicalName();
    }

    public long getAddTime() {
        return addTime;
    }

    public void setTime(long millis) {
        this.addTime = millis;
    }

    protected Class<? extends Download> getClazz() throws ClassNotFoundException {
        return (Class<? extends Download>) Class.forName(classStr);
    }

    protected void setClazz(Class<? extends Download> clazz) {
        this.classStr = clazz.getCanonicalName();
    }

    @Override
    public String toString() {
        return this.fileName;
    }
}
