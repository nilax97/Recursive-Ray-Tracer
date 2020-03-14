import org.json.JSONObject;

public class polygon implements World {

    public int num;
    public vector vertex[];
    public material mat;
    public colour col;
    public vector normal;

    public polygon(JSONObject obj)
    {
        num = obj.getInt("npoints");
        vertex = new vector[num];
        JSONObject vertices = obj.getJSONObject("points");
        for(int i=1;i<=num;++i)
        {
            JSONObject ver = vertices.getJSONObject("points_"+Integer.toString(i));
            vector x = new vector(ver, "");
            vertex[i-1] = x;
        }
        col =  new colour(obj.getJSONObject("colour"), "col");
        mat = new material(obj.getJSONObject("material"), "mat");
    }

   public boolean is_inside(vector a, polygon poly)
    {
        vector vert[] = poly.vertex;
        double angle_sum = 0.0;
        for(int i=0;i<vert.length;++i)
        {
            vector x1 = new vector(vert[i].x - a.x, vert[i].y - a.y, vert[i].z - a.z);
            vector x2 = new vector(vert[(i+1)%num].x - a.x, vert[(i+1)%num].y - a.y, vert[(i+1)%num].z - a.z);
            if(vector.mod(x1)*vector.mod(x2)<0.01)
            {
                return true;
            }
            angle_sum+= Math.acos((vector.dot_product(x1,x2))/(vector.mod(x1)*vector.mod(x2)));
        }
        if(Math.abs(angle_sum - 2*Math.PI)<0.01)
        {
            return true;
        }
        return false;
    }

    public double get_time_intersect(ray r)
    {
        normal = get_normal(r.centre);
        double dist =   -(vector.dot_product(normal,vertex[0]));
        if(Math.abs(vector.dot_product(normal,r.direction))<0.01)
            return 0;
        double t = -((vector.dot_product(normal, r.centre)+dist)/(vector.dot_product(normal, r.direction)));
        if(t<0.01)
        {
            return 0;
        }
        vector inter = get_intersect(r ,t);
        if(is_inside(inter, this))
        {
            return t;
        }
        return 0;

    }
    public vector get_intersect (ray r, double t)
    {
        return new vector(r.centre.x + r.direction.x*t, r.centre.y + r.direction.y*t, r.centre.z + r.direction.z*t);
    }
    public vector get_normal (vector intersection)
    {
        vector x1 = new vector (vertex[1].x - vertex[0].x, vertex[1].y - vertex[0].y, vertex[1].z - vertex[0].z);
        vector x2 = new vector(vertex[2].x - vertex[1].x, vertex[2].y - vertex[1].y, vertex[2].z - vertex[1].z);
        return vector.unit(vector.cross_product(x1,x2));
    }
    public ray ref_ray (ray r, vector inter, vector normal)
    {
        return r;
    }

    public colour getcol() {
        return col;
    }
    public material getmat()
    {
        return mat;
    }
}
