class Vector
{
    /**
      * Draw an arrow line betwwen two point 
      * @param x1 x-position of first point
      * @param y1 y-position of first point
      * @param x2 x-position of second point
      * @param y2 y-position of second point
      * @param d  the width of the arrow
      * @param h  the height of the arrow
      */  

   public int x1, y1, x2, y2, d, h;
   public double vel_X, vel_Y, velocity;
   public int[] xpoints;
   public int[] ypoints;

   Vector(int x1, int y1, int x2, int y2, int d, int h, double vx, double vy, double velocity)
   {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.d = d;
      this.h = h;
      this.vel_X = vx;
      this.vel_Y = vy;
      this.velocity = velocity;
      
      int dx = this.x2 - this.x1; 
      int dy = this.y2 - this.y1;
      double D = Math.sqrt(dx*dx + dy*dy);
      double xm = D - d, xn = xm, ym = h, yn = -h, x;
      double sin = dy/D, cos = dx/D;

      x = xm*cos - ym*sin + x1;
      ym = xm*sin + ym*cos + y1;
      xm = x;

      x = xn*cos - yn*sin + x1;
      yn = xn*sin + yn*cos + y1;
      xn = x;
      
      this.xpoints = new int[]{x2, (int) xm, (int) xn};
      this.ypoints = new int[]{y2, (int) ym, (int) yn};
   }
}