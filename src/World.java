public interface World {
    double get_time_intersect(ray r);
    vector get_intersect (ray r, double t);
    vector get_normal (vector inter);
    ray ref_ray (ray r, vector inter, vector normal);
    colour getcol();
    material getmat();
}
