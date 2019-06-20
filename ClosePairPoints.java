package ass4;
import java.util.*;

public class ClosePairPoints {
    private double[][] points;
    Point p1, p2;

    public ClosePairPoints(double[][] points) {
        this.points = points;
    }

    static class Point implements Comparable<Point> {
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Point p2) {
            if (this.x < p2.x) return -1;
            else if (this.x == p2.x) {
                if (this.y < p2.y) return -1;
                else if (this.y == p2.y) return 0;
                else return 1;
            }
            else return 1;
        }
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public double closestDistance() {
        Point[] xPoints = new Point[points.length];

        for (int i = 0; i < xPoints.length; i++)
            xPoints[i] = new Point(points[i][0], points[i][1]);

        Arrays.sort(xPoints);
        boolean isEqual = false;
        for (int i = 0; i < xPoints.length - 1; i++) {
            if (xPoints[i].compareTo(xPoints[i + 1]) == 0) {
                p1 = xPoints[i];
                p2 = xPoints[i + 1];
                isEqual = true;
            }
        }
        if (isEqual) return 0;

        Point[] yPoints = xPoints.clone();

        return divideAndConquer(xPoints, yPoints,  0,xPoints.length - 1 );
    }


    public double divideAndConquer(Point[] xPoints, Point[] yPoints, int low, int high ) {
        if (low >= high)
            return Double.MAX_VALUE;
        else if (low + 1 == high) {
            p1 = xPoints[low];
            p2 = xPoints[high];
            return distance(xPoints[low], xPoints[high]);
        }
        int mid = (low + high) / 2;
        Point[] yPointsLeft = new Point[mid - low + 1];
        Point[] yPointsRight = new Point[high - mid];
        int j1 = 0; int j2 = 0;
        
        int i = 0;
        while(i<yPoints.length){
        	if (yPoints[i].compareTo(xPoints[mid]) <= 0)
                yPointsLeft[j1++] = yPoints[i];
            else
                yPointsRight[j2++] = yPoints[i];
        	i++;
        }
        
        
        double d1 = divideAndConquer(xPoints, yPointsLeft, low, mid);
        double d2 = divideAndConquer(xPoints, yPointsRight, mid + 1, high);
        double d = Math.min(d1, d2);

        int count = 0;
        i=0;
        while(i < yPointsLeft.length){
        	if (yPointsLeft[i].x >= xPoints[mid].x - d)
                count++;
        	i++;
        }
            
        Point[] stripL = new Point[count];
        count = 0;
        i=0;
        while(i < yPointsLeft.length){
        	if (yPointsLeft[i].x >= xPoints[mid].x - d)
                stripL[count++] = yPointsLeft[i];
        	i++;
        }    
        count = 0;
        i=0;
        while(i < yPointsRight.length){
        	if (yPointsRight[i].x <= xPoints[mid].x + d)
                count++;
        	i++;
        }
            
        Point[] stripR = new Point[count];
        count = 0;
        i=0;
        while(i < yPointsRight.length){
        	 if (yPointsRight[i].x <= xPoints[mid].x + d)
                 stripR[count++] = yPointsRight[i];
        	 i++;
        }
        double d3 = d;
        int j = 0;
        
        for (i = 0; i < stripL.length; i++) {
            while (j < stripR.length && stripL[i].y > stripR[j].y + d)
                j++;

            int k = j;
            while (k < stripR.length && stripR[k].y <= stripL[i].y + d) {
                if (d3 > distance(stripL[i], stripR[k])) {
                    d3 = distance(stripL[i], stripR[k]);
                    p1 = stripL[i];
                    p2 = stripR[k];
                }
                k++;
            }
        }

        return Math.min(d, d3);
    }

    public static void main(String[] args) {
        double[][] points = new double[100][2];

        for (int i = 0; i < points.length; i++) {
            points[i][0] = Math.random() * 100;
            points[i][1] = Math.random() * 100;
        }

        ClosePairPoints ClosePairPoints = new ClosePairPoints(points);
        System.out.println("shortest distance is " +
                ClosePairPoints.closestDistance());
        
        System.out.print("(" + ClosePairPoints.p1.x + ", " +
                ClosePairPoints.p1.y + ") to ");
        System.out.println("(" + ClosePairPoints.p2.x + ", " +
                ClosePairPoints.p2.y + ")");
    }
}