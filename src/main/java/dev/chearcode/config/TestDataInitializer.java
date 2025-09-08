package dev.chearcode.config;

import dev.chearcode.entity.Match;
import dev.chearcode.entity.Player;
import dev.chearcode.repository.MatchRepository;
import dev.chearcode.repository.PlayerRepository;
import org.hibernate.Transaction;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataInitializer {
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    public TestDataInitializer(PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    public void initialize(int matchCount, NameSet nameSet) {
        Transaction transaction = HibernateManager.getSession().beginTransaction();

        try {
            if (!playerRepository.findAll(1, 0).isEmpty()) {
                System.out.println("Test data already exists. Skipping initialization.");
                transaction.commit();
                return;
            }

            System.out.println("Initializing database with " + matchCount + " test matches...");

            Map<String, Player> playerMap = new HashMap<>();
            for (String name : nameSet.names()) {
                Player player = new Player(name);
                UUID id = playerRepository.save(player);
                player.setId(id);
                playerMap.put(name, player);
            }

            ThreadLocalRandom random = ThreadLocalRandom.current();
            List<String> nameList = new ArrayList<>(nameSet.names());

            for (int i = 0; i < matchCount; i++) {
                int firstIndex = random.nextInt(nameList.size());
                int secondIndex = random.nextInt(nameList.size());

                while (firstIndex == secondIndex) {
                    secondIndex = random.nextInt(nameList.size());
                }

                Player player1 = playerMap.get(nameList.get(firstIndex));
                Player player2 = playerMap.get(nameList.get(secondIndex));

                Player winner = random.nextBoolean() ? player1 : player2;

                createMatch(player1, player2, winner);
            }

            transaction.commit();
            System.out.println("Test data initialization completed. Generated " + matchCount + " matches.");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to initialize test data", e);
        }
    }

    private void createMatch(Player player1, Player player2, Player winner) {
        Match match = new Match();
        match.setFirstPlayer(player1);
        match.setSecondPlayer(player2);
        match.setWinner(winner);
        matchRepository.save(match);
    }

    enum NameSet {
        REAL(List.of(
                // top tennis players
                "Roger Federer", "Rafael Nadal", "Novak Djokovic", "Andy Murray", "Stan Wawrinka",
                "Daniil Medvedev", "Alexander Zverev", "Stefanos Tsitsipas", "Carlos Alcaraz", "Jannik Sinner",
                "Serena Williams", "Venus Williams", "Maria Sharapova", "Simona Halep", "Naomi Osaka",
                "Iga Swiatek", "Aryna Sabalenka", "Coco Gauff", "Emma Raducanu", "Bianca Andreescu",

                // legends of tennis
                "Bjorn Borg", "John McEnroe", "Jimmy Connors", "Pete Sampras", "Andre Agassi",
                "Martina Navratilova", "Chris Evert", "Steffi Graf", "Monica Seles", "Justine Henin",

                // modern players
                "Casper Ruud", "Andrey Rublev", "Felix Auger-Aliassime", "Hubert Hurkacz", "Taylor Fritz",
                "Holger Rune", "Ben Shelton", "Sebastian Korda", "Frances Tiafoe", "Tommy Paul",
                "Barbora Krejcikova", "Elena Rybakina", "Ons Jabeur", "Jessica Pegula", "Beatriz Haddad Maia",
                "Daria Kasatkina", "Liudmila Samsonova", "Veronika Kudermetova", "Ekaterina Alexandrova", "Anastasia Pavlyuchenkova"
        )),
        COMICS(List.of(
                // Marvel
                "Spider-Man", "Iron Man", "Captain America", "Thor", "Hulk", "Black Widow", "Hawkeye", "Wolverine", "Deadpool", "Magneto",
                "Professor X", "Storm", "Jean Grey", "Cyclops", "Gambit", "Rogue", "Daredevil", "Doctor Strange", "Black Panther", "Captain Marvel",
                "Loki", "Thanos", "Venom", "Carnage", "Green Goblin", "Doctor Doom", "Red Skull", "Ultron", "Vision", "Scarlet Witch",
                "Ant-Man", "Wasp", "Star-Lord", "Gamora", "Drax", "Rocket Raccoon", "Groot", "Nebula", "Mantis", "Yondu",
                "Moon Knight", "Blade", "Ghost Rider", "Silver Surfer", "Galactus", "Nick Fury", "Maria Hill", "Phil Coulson", "Agent 13", "Shuri",

                // DC
                "Superman", "Batman", "Wonder Woman", "The Flash", "Aquaman", "Green Lantern", "Martian Manhunter", "Cyborg", "Shazam", "Green Arrow",
                "Black Canary", "Supergirl", "Batgirl", "Nightwing", "Robin", "Red Hood", "Catwoman", "Poison Ivy", "Harley Quinn", "Joker",
                "Lex Luthor", "Darkseid", "Doomsday", "Bane", "Two-Face", "Riddler", "Penguin", "Mr. Freeze", "Scarecrow", "Ra's al Ghul",
                "Deathstroke", "Reverse-Flash", "Captain Cold", "Heat Wave", "Gorilla Grodd", "Brainiac", "Sinestro", "Black Adam", "Zatanna", "John Constantine",
                "Swamp Thing", "Blue Beetle", "Booster Gold", "Firestorm", "Hawkman", "Hawkgirl", "Vixen", "Static Shock", "Raven", "Beast Boy"
        ));
        private final List<String> names;

        NameSet(List<String> names) {
            this.names = names;
        }

        protected List<String> names() {
            return this.names;
        }
    }
}
