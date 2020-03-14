import org.json.JSONObject;

public class light {
    public colour col;
    public vector pos;

    public light(JSONObject object)
    {
        col = new colour(object.getJSONObject("colour"), "col");
        pos = new vector(object.getJSONObject("position"), "pos");
    }

    public colour get_col(World world, vector inter, vector norm, vector eye)
    {
        return colour.add_colour(get_diff_col(world, inter, norm), get_spec_col(world, inter, norm, eye),1);
    }

    public colour get_diff_col(World world, vector inter, vector norm)
    {
        vector light_vec = vector.unit(vector.subtraction(pos, inter));
        double co = vector.dot_product(norm, light_vec);
        if(co<0.0)
        {
            return new colour(0,0,0,1);
        }
        co *= world.getmat().k_diffuse;

        colour ans = colour.mult_colour(col,world.getcol(),1);
        return colour.scale_colour(ans, co);
    }

    public colour get_spec_col(World world, vector inter, vector norm, vector eye)
    {
        vector light_vec = vector.unit(vector.subtraction(pos, inter));
        vector ref_vec = vector.scale_vector(norm, 2.0 * vector.dot_product(norm, light_vec));
        ref_vec = vector.unit(vector.subtraction(ref_vec, light_vec));
        vector view_vec = vector.unit(vector.subtraction(eye,inter));
        double co = vector.dot_product(ref_vec,view_vec);
        if(co<0.0)
        {
            return new colour(0,0,0,1);
        }
        co = Math.pow(co,world.getmat().rough);
        co *= world.getmat().k_specular;
        colour ans = colour.mult_colour(col,world.getcol(),1);
        return colour.scale_colour(ans, co);
    }
}
