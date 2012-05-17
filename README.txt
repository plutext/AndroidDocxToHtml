Convert sample.docx to HTML on Android, using docx4j.

When you run it, the app should come up with 2 tabs, one displaying the web page, the other the HTML source.

The docx which is converted is the one in res/raw directory.

If you look at AndroidDocxToHtmlActivity.java you'll see that you can choose to have HTML images saved as files on the device, or based64 encoded as a data URI.  The latter makes the HTML source harder to read, but avoids issues writing to the device (cleanup etc), see  http://dataurl.net/#about 

To run this Eclipse project, you'll probably need to increase your heap space in eclipse.ini (symptom is 'you get Unable to execute dex: Java heap space').  

I used: 
-Xms256m
-Xmx4096m

In Eclipse, Windows > Preferences > General > Show Heap Status gives you an entry on the bottom row which is useful.

Because Android does not support JAXB out of the box, this project contains jars which came from:

    - https://github.com/plutext/jaxb-2_2_5_1/tree/android2
    - https://github.com/plutext/ae-xmlgraphics-commons
    - https://github.com/plutext/ae-awt
    
Note: The set of jars here is the minimal set required for HTML output.  Certain other docx4j features will require additional jars, which are not present in this project.    