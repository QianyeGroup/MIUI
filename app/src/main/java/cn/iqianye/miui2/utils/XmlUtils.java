package cn.iqianye.miui2.utils;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.FileWriter;

public class XmlUtils
{
    public static void xmlSave(String fileName, String height)
    {
        File f = new File(fileName);
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");
        Element root = doc.addElement("MIUI_Theme_Values");
        Element e1;
        e1 = root.addElement("dimen");
        e1.addAttribute("name", "status_bar_height");
        e1.addText(height);
        
        Element e2;
        e2 = root.addElement("dimen");
        e2.addAttribute("name", "status_bar_height_portrait");
        e2.addText(height);
        
        OutputFormat format = new OutputFormat(null, true);
        XMLWriter output;
        try
        {
            output = new XMLWriter(new FileWriter(f), format);
            output.write(doc); 
            output.close();
        }
        catch (Exception e)
        {}
    }

}
