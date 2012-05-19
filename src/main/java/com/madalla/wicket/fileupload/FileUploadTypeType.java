package com.madalla.wicket.fileupload;

public enum FileUploadTypeType {

    TYPE_PDF("application/pdf", "pdf", "Adobe PDF"), 
    TYPE_DOC("application/msword", "doc", "Word document");
    // TYPE_ODT("application/vnd.oasis.opendocument.text", "odt", "ODT document");

    public final String mimeType; // Mime Type
    public final String suffix; // File suffix
    public final String description;

    FileUploadTypeType(String mimeType, String suffix, String description) {
        this.mimeType = mimeType;
        this.suffix = suffix;
        this.description = description;
    }

    /**
     * @param s
     *            the separator
     * @return Separated list of suffixes e.g. doc,pdf,htm
     */
    public static String getResourceTypeSuffixes(String s) {
        StringBuffer sb = new StringBuffer();
        for (FileUploadTypeType type : FileUploadTypeType.values()) {
            sb.append(s + type.suffix);
        }
        return sb.toString().substring(1);
    }

    public static FileUploadTypeType getByMimeType(final String mimeType) {
        for (FileUploadTypeType type : FileUploadTypeType.values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        return null;
    }

}
