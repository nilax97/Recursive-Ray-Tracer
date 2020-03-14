import org.json.JSONObject;

public class colour {
    public int red;
    public int green;
    public int blue;
    public int num;

    public colour(int x,int y, int z, int n)
    {
        red = x;
        green = y;
        blue = z;
        num = n;
    }

    public colour(JSONObject object, String key)
    {
        red = object.getInt(key+"_r");
        green = object.getInt(key+"_g");
        blue = object.getInt(key+"_b");
        num = 1;
    }

    static colour add_colour(colour x, colour y, int num)
    {
        if(num==1)
        {
            num = 2;
        }
        if(x==null)
        {
            return y;
        }
        else if(y==null)
        {
            return x;
        }
        else
        {
            return new colour(x.red + y.red, x.green + y.green, x.blue + y.blue, x.num + y.num );
        }
    }

    static colour mult_colour (colour x, colour y, int num)
    {
        if(num==1)
        {
            num=2;
        }
        num = x.num * y.num * 255;
        return new colour(x.red * y.red / num, x.green * y.green / num, x.blue * y.blue / num, 1);
    }

    static colour mult_colour (colour x, double num)
    {
        if(x==null)
        {
            return null;
        }
        return new colour ( (int)(num * (double)(x.red/x.num)), (int)(num * (double)(x.green/x.num)), (int)(num * (double)(x.blue/x.num)), 1 );
    }

    static colour scale_colour(colour x, double scalar)
    {
        if(x==null)
        {
            return null;
        }
        return new colour(  (int)(scalar * (double)(x.red/x.num)),
                (int)(scalar * (double)(x.green/x.num)),
                (int)(scalar * (double)(x.blue/x.num)),1 );
    }

    public int get_code()
    {
        return ((red/num) << 16) + (green/num << 8) + blue/num;
    }

}