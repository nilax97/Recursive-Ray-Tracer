public class coordinate {
    public vector view_ref;
    public vector u;
    public vector v;
    public vector n;

    public coordinate (vector view_reference, vector normal, vector up)
    {
        view_ref = view_reference;
        n=vector.scale_vector(normal, 1/vector.mod(normal));
        up=vector.addition(up, vector.scale_vector(n,-1*vector.dot_product(up,n)));
        v = vector.scale_vector(up,1/vector.mod(up));
        u = vector.cross_product(n,v);
    }

    public vector VCStoWCS(vector a)
    {
        return new vector(view_ref.x + a.x * u.x + a.y * v.x + a.z * n.x, view_ref.y + a.x * u.y + a.y * v.y + a.z * n.y, view_ref.z + a.x * u.z + a.y * v.z + a.z * n.z);
    }
}
