import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Triangle implements Shape
{
    double centerX;
    double centerY;
    double median23;

    public Triangle(double centerX, double centerY, double median23)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.median23 = median23;

        if (median23 < 0)
            throw new IllegalArgumentException("Line must be >= 0");
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) (centerX - median23),
                (int) (centerY - median23), (int) (2 * median23),
                (int) (2 * median23));
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return new Rectangle2D.Double(centerX - median23, centerY - median23,
                2 * median23, 2 * median23);
    }

    @Override
    public boolean contains(double x, double y)
    {
        double x1 = centerX;
        double y1 = centerY - median23;
        double x2 = centerX - median23 * Math.sin(Math.PI / 3);
        double y2 = centerY + median23 * Math.sin(Math.PI / 3);
        double x3 = centerX + median23 * Math.sin(Math.PI / 3);
        double y3 = centerY + median23 * Math.sin(Math.PI / 3);
        double a = (x1 - x) * (y2 - y1) - (x2 - x1) * (y1 - y);
        double b = (x2 - x) * (y3 - y2) - (x3 - x2) * (y2 - y);
        double c = (x3 - x) * (y1 - y3) - (x1 - x3) * (y3 - y);

        if ((a >= 0 && b >= 0 && c >= 0) || (a <= 0 && b <= 0 && c <= 0))
            return true;
        else
            return false;
    }

    @Override
    public boolean contains(Point2D p)
    {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean intersects(double x, double y, double w, double h)
    {
        return getBounds().intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r)
    {
        return getBounds().intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h)
    {
        return contains(x, y) && contains(x + w, y) && contains(x, y + h)
                && contains(x + w, y + h);
    }

    @Override
    public boolean contains(Rectangle2D r)
    {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at)
    {
        return new TriangleIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return getPathIterator(at);
    }

    class TriangleIterator implements PathIterator
    {
        int index = 0;
        boolean done = false;
        private AffineTransform at;

        public TriangleIterator(AffineTransform at)
        {
            this.at = at;
        }

        @Override
        public int getWindingRule()
        {
            return WIND_NON_ZERO;
        }

        @Override
        public boolean isDone()
        {
            return done;
        }

        @Override
        public void next()
        {
            index++;

        }

        @Override
        public int currentSegment(float[] coords)
        {
            if (index == 0)
            {
                coords[0] = (float) centerX;
                coords[1] = (float) (centerY - median23);
                if (at != null)
                    at.transform(coords, 0, coords, 0, 1);
                return SEG_MOVETO;
            }
            if (index == 1)
            {
                coords[0] = (float) (centerX - median23 * Math.sin(Math.PI / 3));
                coords[1] = (float) (centerY + median23 * Math.cos(Math.PI / 3));
            } else if (index == 2)
            {
                coords[0] = (float) (centerX + median23 * Math.sin(Math.PI / 3));
                coords[1] = (float) (centerY + median23 * Math.cos(Math.PI / 3));
            } else
            {
                coords[0] = (float) (centerX);
                coords[1] = (float) (centerY - median23);
                done = true;
            }
            if (at != null)
                at.transform(coords, 0, coords, 0, 1);
            return SEG_LINETO;
        }

        @Override
        public int currentSegment(double[] coords)
        {
            if (index == 0)
            {
                coords[0] = centerX;
                coords[1] = (centerY - median23);
                if (at != null)
                    at.transform(coords, 0, coords, 0, 1);
                return SEG_MOVETO;
            }
            if (index == 1)
            {
                coords[0] = (centerX - median23 * Math.sin(Math.PI / 3));
                coords[1] = (centerY + median23 * Math.cos(Math.PI / 3));
            } else if (index == 2)
            {
                coords[0] = (centerX + median23 * Math.sin(Math.PI / 3));
                coords[1] = (centerY + median23 * Math.cos(Math.PI / 3));
            } else
            {
                coords[0] = (centerX);
                coords[1] = (centerY - median23);
                done = true;
            }
            if (at != null)
                at.transform(coords, 0, coords, 0, 1);
            return SEG_LINETO;
        }

    }
}