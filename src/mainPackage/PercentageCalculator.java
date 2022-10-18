package mainPackage;

public class PercentageCalculator {

    public double teamATime;
    public double teamBTime;
    public double teamAPercentage;
    public double teamBPercentage;

    public void calculate(){
        double sum = teamATime + teamBTime;
        teamAPercentage = ((teamATime/sum) * 100);
        teamBPercentage = ((teamBTime/sum) * 100);
    }
    
    public void reset(){
        teamATime = 0.0;
        teamBTime = 0.0;
        teamAPercentage = 0.0;
        teamBPercentage = 0.0;
    }
}
