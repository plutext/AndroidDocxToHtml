package org.plutext.DocxToHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.docx4j.model.images.AbstractConversionImageHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;

import android.app.Activity;
import android.content.Context;

/** The DefaultConversionImageHandler is a pure File-based ImageHandler. 
 * 
 */
public class AndroidFileConversionImageHandler extends AbstractConversionImageHandler {
	
	/** Creates a DefaultConversionImageHandler.
	 * @param imageDirPath 
	 * @param targetUri
	 * @param includeUUID
	 */
	private AndroidFileConversionImageHandler(String imageDirPath, boolean includeUUID) {
		super(imageDirPath, includeUUID);
	}

	/** Creates as HTMLConversionImageHandler
	 * 
	 * @param imageDirPath, the target path where images are stored 
	 * @param targetUri, the uri prefix that will be used in the generated HTML
	 * @param includeUUID, if a uuid should be included in the image name to differentiate the images of different runs
	 */
	public AndroidFileConversionImageHandler(String imageDirPath, String targetUri, boolean includeUUID, Activity activity) {
		super(imageDirPath, includeUUID);
		this.targetUri = targetUri;
		this.activity = activity;
	}
	
	Activity activity;
	
	protected String targetUri = null;
	
	@Override
	protected String createStoredImage(BinaryPart binaryPart, byte[] bytes) throws Docx4JException {
		String uri = null;
		// To create directory:
		File folder = activity.getDir(imageDirPath, Context.MODE_WORLD_READABLE); 
		// Not allowed to provide a path separator!
		// File app_/data/data/com.example.HelloAndroid/files contains a path separator

		// Construct a file name from the part name
		String filename = setupImageName(binaryPart);
		log.debug("image file name: " + filename);

		uri = storeImage(binaryPart, bytes, folder, filename);
		return uri;
	}

	protected String storeImage(BinaryPart binaryPart, byte[] bytes, File folder, String filename) throws Docx4JException {
	String uri = null;
	File imageFile = new File(folder, filename);
	FileOutputStream out = null;
	
		log.info("Writing: " + imageFile.getAbsolutePath() );
		if (imageFile.exists()) {
			log.warn("Overwriting (!) existing file!");
		}
		try {
			out = new FileOutputStream(imageFile);
			out.write(bytes);
			
			// return the uri
			uri = setupImageUri(imageFile);
			log.info("Wrote @src='" + uri);
		} catch (IOException ioe) {
			throw new Docx4JException("Exception storing '" + filename + "', " + ioe.toString(), ioe);
		} finally {
			try {
				out.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}					
		}
		return uri;
	}
	
	
	/** If there is a prefix use this prefix for the uri
	 */
    protected String setupImageUri(File imageFile) {
    String uri = null;
    	if ((targetUri == null) || (targetUri.length() == 0)) {
    		uri = imageFile.getName();
    	}
    	else {
    		uri = ((targetUri.charAt(targetUri.length() - 1) != '/') ?
    				targetUri + '/' + imageFile.getName() :
    				targetUri + imageFile.getName());
    	}
    	return uri;
    }
	//out = activity.openFileOutput(filename, Context.MODE_WORLD_READABLE);
	
}
    