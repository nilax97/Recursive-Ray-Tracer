import org.json.JSONObject;

public class sphere implements World {

    public vector centre;
    public double radius;
    public colour col;
    public material mat;

    public sphere(JSONObject obj)
    {
        centre = new vector(obj.getJSONObject("centre"), "centre");
        radius = obj.getDouble("radius");
        col =  new colour(obj.getJSONObject("colour"), "col");
        mat = new material(obj.getJSONObject("material"), "mat");
    }
    @SuppressWarnings("Duplicates")
    public double get_time_intersect(ray r)
    {
        double a = 1.0;
        double b = 2.0 * (r.direction.x*(r.centre.x - centre.x) + r.direction.y*(r.centre.y - centre.y) + r.direction.z*(r.centre.z - centre.z));
        double c = (r.centre.x - centre.x)*(r.centre.x - centre.x) + (r.centre.y - centre.y)*(r.centre.y - centre.y) + (r.centre.z - centre.z)*(r.centre.z - centre.z) - radius*radius;

        double d = b*b - 4*a*c;
        if(d<0.0)
        {
            return 0;
        }
        double t1 = (-1*b + Math.sqrt(d))/(2*a);
        double t2 = (-1*b - Math.sqrt(d))/(2*a);
        if(t1<0.01 && t2<0.01)
        {
            return 0.0;
        }
        else if(t1>=0.01 && t2<0.01)
        {
            return t1;
        }
        else if(t2>=0.01 && t1<0.01)
        {
            return t2;
        }
        else
        {
            return Math.min(t1,t2);
        }
    }
    public vector get_intersect (ray r, double t)
    {
        return new vector(r.centre.x + r.direction.x*t, r.centre.y + r.direction.y*t, r.centre.z + r.direction.z*t);
    }
    public vector get_normal (vector intersection)
    {
        vector x = vector.addition(intersection, vector.scale_vector(centre,-1.0));
        return vector.scale_vector(x, (1.0/vector.mod(x)));
    }
    public ray ref_ray (ray r, vector inter, vector normal)
    {
        ray ref_ray = ref_sur(r, inter, normal, mat, mat.ref_index);
        vector p = get_intersect(ref_ray, get_time_intersect(ref_ray));
        vector nor = vector.scale_vector (get_normal(p),-1.0);
        return ref_sur(ref_ray, p, nor, mat, 1.0/mat.ref_index);
    }
    @SuppressWarnings("Duplicates")
    public ray ref_sur (ray i, vector inter, vector normal, material mat, double ref)
    {
        double i_angle = Math.acos(-1.0 * vector.dot_product(i.direction, normal));
        double r_angle = Math.asin(Math.sin(i_angle)/ref);
        vector t1 = vector.addition(i.direction, vector.scale_vector(normal,Math.cos(i_angle)));
        vector t2 = vector.scale_vector(t1, 1.0/ref);
        vector t3 = vector.scale_vector(normal, Math.cos(r_angle));
        vector ref_dir = vector.addition(t2, vector.scale_vector(t1,-1.0));
        ref_dir = vector.scale_vector(ref_dir, vector.mod(ref_dir));
        return new ray(inter, ref_dir);
    }
    public colour getcol() {
        return col;
    }
    public material getmat()
    {
        return mat;
    }
}
