package org.plutext.DocxToHtml;


import java.io.InputStream;

import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.HtmlExporterNonXSLT;
import org.docx4j.model.images.ConversionImageHandler;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TextView;

public class AndroidDocxToHtmlActivity extends  TabActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        InputStream is = this.getResources().openRawResource(R.raw.sample);
        
        final long startTime = System.currentTimeMillis(); 
        final long endTime; 
        try { 
			final LoadFromZipNG loader = new LoadFromZipNG();
			WordprocessingMLPackage wordMLPackage = (WordprocessingMLPackage)loader.get(is);
			
			String IMAGE_DIR_NAME = "images";

			String baseURL = this.getDir(IMAGE_DIR_NAME, Context.MODE_WORLD_READABLE).toURL().toString();
			System.out.println(baseURL); // file:/data/data/com.example.HelloAndroid/app_images/
			
			// Uncomment this to write image files to file system
			ConversionImageHandler conversionImageHandler = new AndroidFileConversionImageHandler( IMAGE_DIR_NAME, // <-- don't use a path separator here
					baseURL, false, this);

			// Uncomment to use a base 64 encoded data URI for each image  
//			ConversionImageHandler conversionImageHandler = new AndroidDataUriImageHandler();
			
			HtmlExporterNonXSLT withoutXSLT = new HtmlExporterNonXSLT(wordMLPackage, conversionImageHandler); 
			
			String html = XmlUtils.w3CDomNodeToString(withoutXSLT.export());
			
	        TextView tv = (TextView)this.findViewById(R.id.sourceview);	        
	        tv.setText(html);	
	        tv.setMovementMethod(new ScrollingMovementMethod()); 
						
			WebView webview = (WebView)this.findViewById(R.id.webpage); 
			webview.loadDataWithBaseURL(baseURL, html , "text/html", null, null);

	        TabHost mTabHost = getTabHost();        
	        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Web Page").setContent(R.id.webpage));        
	        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("View Source").setContent(tv.getId()));    
	        mTabHost.setCurrentTab(0);
						 
		} catch (Exception e) {
			e.printStackTrace();
	        finish();
        } finally { 
          endTime = System.currentTimeMillis(); 
        } 
        final long duration = endTime - startTime; 
        System.err.println("Total time: " + duration + "ms");
    }
}