import org.json.JSONObject;

public class material {
    public double k_ambeint;
    public double k_diffuse;
    public double k_specular;
    public double k_reflect;
    public double k_refract;
    public int rough;
    public double ref_index;

    material(JSONObject mat, String str)
    {
        k_ambeint = mat.getDouble(str + "_ka");
        k_diffuse = mat.getDouble(str + "_kd");
        k_specular = mat.getDouble(str + "_ks");
        k_reflect = mat.getDouble(str + "_krg");
        k_refract = mat.getDouble(str + "_ktg");
        rough = mat.getInt(str + "_rough");
        ref_index = mat.getDouble(str + "_index");
    }

}
