package assignment2;

import java.util.*;
class Player {
    String name;
    int points;
    int goalDifference;
    Player(String name, int points, int goalDifference) {
        this.name = name;
        this.points = points;
        this.goalDifference = goalDifference;
    }
    public String toString() {
        return name + " - Points: " + points
                + ", Goal Difference: " + goalDifference;
    }
}
public class LeaderboardDemo {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Ravi", 30, 15));
        players.add(new Player("Arun", 25, 10));
        players.add(new Player("Kumar", 30, 20));
        players.add(new Player("Ajay", 30, 20));
        players.add(new Player("Vijay", 25, 12));
        Comparator<Player> comparator =
                Comparator.comparingInt((Player p) -> p.points)
                .reversed()
                .thenComparing(
                    Comparator.comparingInt(
                        (Player p) -> p.goalDifference
                    ).reversed()
                )
                .thenComparing(p -> p.name);
        players.sort(comparator);
        System.out.println("Leaderboard:");
        for (Player p : players) {
            System.out.println(p);
        }
        List<Player> secondList = new ArrayList<>();
        secondList.add(new Player("John", 20, 5));
        secondList.add(new Player("David", 35, 15));
        secondList.add(new Player("Alex", 35, 20));
        secondList.sort(comparator);
        System.out.println("\nSecond Leaderboard:");
        for (Player p : secondList) {
            System.out.println(p);
        }
    }
}
