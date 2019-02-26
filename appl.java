import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.applet.Applet;
import java.awt.image.BufferedImage;
public class appl extends Applet
{
    Color bgr;
    Color col;
    Color fillCol1;
    Color fillCol2;
    private Shape triangle;
    private int unitWidth = 400;
    private int unitHeight = 300;
    private int centerX = unitWidth / 2;
    private int centerY = unitHeight / 2;
    private int median23 = unitHeight / 3;

    public void init()
    {

        triangle = new Triangle(centerX, centerY, median23);
        bgr=new Color(255,255,255);
        fillCol1 = new Color(64, 64, 64);
        fillCol2=new Color(230, 230, 230);
        col=new Color(49, 44,235);
    }
    static byte[] brightenTable = new byte[256];
    static byte[] thresholdTable = new byte[256];
    static {  // Initialize the arrays
        for(int i = 0; i < 256; i++) {
            brightenTable[i] = (byte)(Math.sqrt(i/255.0)*255);
            thresholdTable[i] = (byte)((i < 225)?0:i);
        }
    }


    public void paint(Graphics g)
    {

       // BufferedImage bimage = new BufferedImage(WIDTH/2, HEIGHT, BufferedImage.TYPE_INT_RGB);
        setSize(900,900);
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage original = initOriginal();
        g2d.drawImage(original, 0, 0, null);

        //должен работать этот фильтр, но он единственный не работает
        //g2d.drawImage(new RescaleOp(-1.0f, 255f, null).filter(original, null), unitWidth, 0, null);

        /*g2d.drawImage(new ConvolveOp(new Kernel(3, 3, new float[] {
                0.0f, -0.75f, 0.0f,
                -0.75f, 4.0f, -0.75f,
                0.0f, -0.75f, 0.0f})).filter(original, null), unitWidth,0, null)*/
        /*g2d.drawImage(new ConvolveOp(new Kernel(3, 3, new float[] {
                .1111f,.1111f,.1111f,
                .1111f,.1111f,.1111f,
                .1111f,.1111f,.1111f,})).filter(original, null), unitWidth, 0, null);*/
        g2d.drawImage(new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(original, null), unitWidth, 0, null);
        //g2d.drawImage(new LookupOp(new ByteLookupTable(0, brightenTable), null).filter(original, null), unitWidth, 0, null);
        //g2d.drawImage(new LookupOp(new ByteLookupTable(0, thresholdTable), null).filter(original, null), unitWidth, 0, null);
        //g2d.drawImage(original, unitWidth, 0, null);

    }


    BufferedImage initOriginal()
    {
        BufferedImage biSrc = new BufferedImage(unitWidth, unitHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = biSrc.createGraphics();


        g2d.setBackground(bgr);
        g2d.clearRect(0, 0, unitWidth, unitHeight);

        Paint shadowPaint = new Color(80, 80, 80, 120);
        AffineTransform shadowTransform = AffineTransform.getShearInstance(-2.5, 0.0);
        shadowTransform.scale(1.0, 0.5);
        g2d.setPaint(shadowPaint);
        g2d.translate(250, 100);
        g2d.fill(shadowTransform.createTransformedShape(triangle));
        g2d.translate(-250, -100);

        g2d.setPaint(new GradientPaint(centerX, centerY + median23 / 2,
                fillCol1, centerX, centerY - median23, fillCol2));
        g2d.fill(triangle);

        g2d.setColor(col);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(triangle);

        Font font = new Font("Serif", Font.BOLD, 8);
        Font bigfont = font.deriveFont(AffineTransform.getScaleInstance(18.0,18.0));
        GlyphVector gv = bigfont.createGlyphVector(g2d.getFontRenderContext(), "!");
        Shape jshape = gv.getGlyphOutline(0);
        g2d.setColor(col);
        g2d.translate(175, 185);
        g2d.fill(jshape);
        g2d.translate(-175, -185);

        return biSrc;

    }
}