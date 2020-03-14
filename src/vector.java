import org.json.JSONObject;

public class vector {
    public double x;
    public double y;
    public double z;

    public vector()
    {
        x=0;
        y=0;
        z=0;
    }

    public vector(double a, double b, double c)
    {
        x=a;
        y=b;
        z=c;
    }

    public vector (JSONObject Object, String key)
    {
        x = Object.getDouble(key + "_x");
        y = Object.getDouble(key + "_y");
        z = Object.getDouble(key + "_z");
    }

    static double mod (vector a)
    {
        return Math.sqrt(a.x*a.x +a.y*a.y +a.z*a.z);
    }

    static vector scale_vector (vector v, double a)
    {
        return new vector(v.x*a, v.y*a, v.z*a);
    }

    static vector addition (vector a, vector b)
    {
        return new vector(a.x+b.x, a.y+b.y, a.z+b.z);
    }

    static double dot_product (vector a, vector b)
    {
        return (a.x*b.x + a.y*b.y + a.z*b.z);
    }

    static vector cross_product (vector m, vector n)
    {
        return new vector((m.y * n.z) - (m.z * n.y),(m.z * n.x) - (m.x * n.z),(m.x * n.y) - (m.y * n.x));
    }

    static vector subtraction (vector a, vector b)
    {
        return new vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    static vector unit(vector a)
    {
        return scale_vector(a, 1.0/mod(a));
    }
}
