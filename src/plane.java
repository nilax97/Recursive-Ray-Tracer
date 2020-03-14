import org.json.JSONObject;

public class plane implements World{
    public vector point;
    public vector normal;
    public material mat;
    public colour col;

    public plane (JSONObject obj)
    {
        point = new vector(obj.getJSONObject("point"),"point");
        normal = new vector(obj.getJSONObject("normal"),"normal");
        mat = new material(obj.getJSONObject("material"),"mat");
        col = new colour(obj.getJSONObject("colour"),"col");
    }

    public double get_time_intersect(ray r)
    {
        double dist = - (vector.dot_product(normal,point));
        if(Math.abs(vector.dot_product(normal,r.direction))<0.01)
        {
            return 0;
        }
        double t = -((vector.dot_product(normal, r.centre)+dist)/(vector.dot_product(normal, r.direction)));
        if(t<0.01)
        {
            return 0.0;
        }
        return t;
    }

    public vector get_intersect(ray r, double t)
    {
        return new vector(r.centre.x + r.direction.x*t, r.centre.y + r.direction.y*t, r.centre.z + r.direction.z*t);
    }

    public ray ref_ray(ray r, vector inter, vector normal)
    {
        return r;
    }


    public vector get_normal (vector intersection)
    {
        return normal;
    }

    public colour getcol() {
        return col;
    }
    public material getmat()
    {
        return mat;
    }
}
