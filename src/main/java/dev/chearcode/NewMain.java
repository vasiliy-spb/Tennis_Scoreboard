package dev.chearcode;

import dev.chearcode.new_model.Match;
import dev.chearcode.new_model.Player;

import java.util.Scanner;
import java.util.UUID;

public class NewMain {
    public static void main(String[] args) {
        Player firstPlayer = new Player(new UUID(0, 31), "Первый");
        Player secondPlayer = new Player(new UUID(0, 31), "Второй");

        Match match = new Match(new UUID(0, 31), firstPlayer, secondPlayer);
        match.showResult();

        Scanner scanner = new Scanner(System.in);
        while (!match.isGameOver()) {
            System.out.println("Выберите победителя:");
            System.out.printf("1 - Player: %s\n", firstPlayer.getName());
            System.out.printf("2 - Player: %s\n", secondPlayer.getName());
            try {
                int winNumber = Integer.parseInt(scanner.nextLine());
                if (winNumber == 1) {
                    match.addPoint(firstPlayer);
                } else if (winNumber == 2) {
                    match.addPoint(secondPlayer);
                } else {
                    System.out.println("Нет игрока с таким номером..");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }

            match.showResult();
        }

    }
}
