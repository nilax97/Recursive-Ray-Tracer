import org.json.JSONObject;

public class quadric implements World {

    public double a;
    public double b;
    public double c;
    public double d;
    public double e;
    public double f;
    public double g;
    public double h;
    public double i;
    public double j;
    public material mat;
    public colour col;

    public quadric(JSONObject obj)
    {
        JSONObject val = obj.getJSONObject("values");
        a = val.getDouble("_a");
        b = val.getDouble("_b");
        c = val.getDouble("_c");
        d = val.getDouble("_d");
        e = val.getDouble("_e");
        f = val.getDouble("_f");
        g = val.getDouble("_g");
        h = val.getDouble("_h");
        i = val.getDouble("_i");
        j = val.getDouble("_j");
        col =  new colour(obj.getJSONObject("colour"), "col");
        mat = new material(obj.getJSONObject("material"), "mat");
    }
    @SuppressWarnings("Duplicates")
    public double get_time_intersect(ray r)
    {
        double A = a*r.direction.x*r.direction.x + b*r.direction.y*r.direction.y + c*r.direction.z*r.direction.z
                + d*r.direction.x*r.direction.y + e*r.direction.x*r.direction.z + f*r.direction.y*r.direction.z;
        double B = 2.0*a*r.centre.x*r.direction.x + 2.0*b*r.centre.y*r.direction.y + 2.0*c*r.centre.z*r.direction.z
                + d*(r.direction.x*r.centre.y + r.direction.y*r.centre.x) + + e*(r.direction.x*r.centre.z + r.direction.z*r.centre.x) + f*(r.direction.z*r.centre.y + r.direction.y*r.centre.z) +
                g*r.direction.x + h*r.direction.y + i*r.direction.z;
        double C = a*r.centre.x*r.centre.x + b*r.centre.y*r.centre.y + c*r.centre.z*r.centre.z +
                d*r.centre.x*r.centre.y + e*r.centre.x*r.centre.z + f*r.centre.y*r.centre.z +
                g*r.centre.x + h*r.centre.y + i*r.centre.z + j;

        double D = B*B - 4.0*A*C;
        if(D<0.0)
        {
            return 0;
        }
        if(A==0)
        {
            return -1 * (C/B);
        }
        double t1 = (-1.0*B + Math.sqrt(D))/(2.0*A);
        double t2 = (-1.0*B - Math.sqrt(D))/(2.0*A);
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
    public vector get_normal (vector inter)
    {
        double x1 = 2.0*a*inter.x + d*inter.y + e*inter.z +g;
        double x2 = 2.0*b*inter.y + d*inter.x + f*inter.z +h;
        double x3 = 2.0*c*inter.z + f*inter.y + e*inter.x +i;
        vector x = new vector(x1,x2,x3);
        x = vector.unit(x);
        return x;
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
    public colour getcol()
    {
        return col;
    }
    public material getmat()
    {
        return mat;
    }
}
