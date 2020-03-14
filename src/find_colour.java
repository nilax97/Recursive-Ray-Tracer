import java.util.ArrayList;

public class find_colour {
    public ArrayList<World> objects;
    public ArrayList<light> lig;
    public colour ambient;
    public vector eye;

    public find_colour(ArrayList<World> objs, ArrayList<light> ligs, colour ambi, vector eye_vec)
    {
        objects = objs;
        lig = ligs;
        ambient = ambi;
        eye = eye_vec;
    }

    public World find_inter_world (ray r, ArrayList<World> objs)
    {
        World inter = null;
        double min_time = Double.MAX_VALUE;
        for(World obj:objs)
        {
            double time = obj.get_time_intersect(r);
            if(time>0.0 && time<min_time)
            {
                inter = obj;
                min_time = time;
            }
        }
        return inter;
    }

    public double find_time(ray r, ArrayList<World> objs)
    {
        double min_time = Double.MAX_VALUE;
        for(World obj:objs)
        {
            double time = obj.get_time_intersect(r);
            if(time>0.0 && time<min_time)
            {
                min_time = time;
            }
        }
        return min_time;
    }

    colour find(ray r, int d)
    {
        if(d==0)
        {
            return null;
        }
        World inter = find_inter_world(r,objects);
        if(inter==null)
        {
            return null;
        }
        double time = inter.get_time_intersect(r);
        vector intersect = inter.get_intersect(r, time);
        vector normal = inter.get_normal(intersect);
        if(vector.dot_product(normal,r.direction)<0)
        {
            vector.scale_vector(normal,-1.0);
        }

        colour cols = colour.mult_colour(ambient, inter.getcol(), 1);
        for(light lights : lig)
        {
            if(d==4)
            {
                double light_time = vector.mod(vector.subtraction(lights.pos, intersect));
                vector shadow = vector.unit(vector.subtraction(lights.pos, intersect));
                ray shadow_ray =  new ray(intersect, shadow);
                ArrayList<World> list_one = new ArrayList<>(objects);
                list_one.remove(inter);
                World obstruct = find_inter_world(shadow_ray, list_one);
                double time_obstruct = find_time(shadow_ray, list_one);
                if(obstruct!=null && time_obstruct<light_time)
                    continue;
            }
            cols = colour.add_colour(cols, lights.get_col(inter, intersect, normal, eye),1);
        }
        if(inter.getmat().k_reflect>0.01)
        {
            vector ref_direct = vector.scale_vector(normal, -2.0*vector.dot_product(r.direction,normal));
            ref_direct = vector.unit(vector.addition(r.direction, ref_direct));
            ray reflect_ray = new ray(intersect, ref_direct);
            colour ref_col = find(reflect_ray, d-1);

            if(ref_col==null)
            {
                ref_col = new colour(0,0,0,1);
            }

            cols = colour.add_colour(cols,ref_col,1);
        }
        if(inter.getmat().k_refract>0.01)
        {
            ray refract = inter.ref_ray(r, intersect, normal);
            colour refract_col = find(refract,d-1);
            if(refract_col==null)
            {
                refract_col = new colour(0,0,0,1);
            }
            colour.scale_colour(refract_col,inter.getmat().k_refract);
            cols = colour.add_colour(cols,refract_col,1);
        }
        return cols;
    }
}
