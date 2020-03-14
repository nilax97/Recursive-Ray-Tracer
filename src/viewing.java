import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class viewing {
        public coordinate vcs;
        public vector eye;
        public int width;
        public int height;
        public ArrayList<World> objects;
        public ArrayList<light> lights;
        public colour ambient;

        viewing(String input_file) throws FileNotFoundException
        {
            String jsonobj = new Scanner(new File(input_file)).useDelimiter("\\Z").next();
            JSONObject root = new JSONObject(jsonobj);

            JSONObject view = root.getJSONObject("view_reference");
            vector view_ref = new vector(view, "ref");
            JSONObject view_plane = root.getJSONObject("view_plane");
            vector normal = new vector(view_plane, "normal");
            JSONObject up_vec = root.getJSONObject("up_vector");
            vector up = new vector(up_vec, "up");
            vcs = new coordinate(view_ref, normal, up);
            double eye_dist = root.getDouble("eye");
            eye = vcs.VCStoWCS(new vector(0, 0, -1 * eye_dist));
            width = root.getInt("columns");
            height = root.getInt("rows");

            objects = new ArrayList<>();
            JSONArray entities = root.getJSONArray("entities");
            for (int i = 0; i < entities.length(); ++i) {
                JSONObject entity = entities.getJSONObject(i);
                if (entity.has("sphere")) {
                    objects.add(new sphere(entity.getJSONObject("sphere")));
                }
                if (entity.has("polygon")) {
                    objects.add(new polygon(entity.getJSONObject("polygon")));
                }
                if (entity.has("plane")) {
                    objects.add(new plane(entity.getJSONObject("plane")));
                }
                if (entity.has("quadric")) {
                    objects.add(new quadric(entity.getJSONObject("quadric")));
                }
            }
            ambient = new colour(root.getJSONObject("ambient"), "col");
            this.lights = new ArrayList<>();
            JSONArray lighter = root.getJSONArray("lights");
            for (int i = 0; i < lighter.length(); i++) {
                JSONObject light = lighter.getJSONObject(i);
                lights.add(new light(light));
            }
        }

        void render (String out_file, int sample_size) throws IOException
        {
            int screen_width = sample_size*width;
            int screen_height = sample_size*height;

            BufferedImage image = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_INT_RGB);
            double left = -width/2, right = width/2;
            double top = height/2, bottom = -height/2;
            double hor = (right - left)/(double)screen_width;
            double ver = (top - bottom)/(double)screen_height;

            for(int i=0;i<screen_width;++i) {
                for (int j = 0; j < screen_height; ++j)
                {
                    vector ray_direction = vcs.VCStoWCS(new vector(left + (hor * i), top - (ver * j), 0.0));
                    ray_direction = vector.unit(vector.subtraction(ray_direction, eye));
                    ray r = new ray(eye, ray_direction);
                    find_colour finder = new find_colour(objects, lights, ambient, eye);
                        colour Col = finder.find(r,4);

                    if (Col == null)
                    {
                        continue;
                    }
                    image.setRGB(i,j,Col.get_code());
                }
            }
            image = supersample(image, sample_size);

            ImageIO.write(image, "png", new File(out_file));
        }

        public BufferedImage supersample(BufferedImage image, int sample_size)
        {
            BufferedImage new_image =new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for(int i=0;i<width;++i)
            {
                for(int j=0;j<height;++j)
                {
                    colour col = null;
                    for(int x=0; x<sample_size;++x)
                    {
                        for(int y=0; y<sample_size;++y)
                        {
                            int a = sample_size*i+x;
                            int b = sample_size*j+y;
                            int new_col = image.getRGB(a,b);
                            int blue = new_col & 255;
                            int green = (new_col>>8) & 255;
                            int red = (new_col>>16) & 255;
                            col = colour.add_colour(col, new colour(red, green, blue, 1),1);
                        }
                    }
                    new_image.setRGB(i,j,col.get_code());
                }
            }
            return new_image;
        }
}
