package PokerGame;

import java.util.*;
import java.util.stream.Collectors;

public class DealerDeal implements Dealer {
    private final Deck deck;

    public DealerDeal() {
        this.deck = new Deck();
    }

    @Override
    public Board dealCardsToPlayers() {
        if (deck.size() < 4) {
            throw new IllegalStateException("Not enough cards to deal to players.");
        }
        String playerOne = deck.dealCard().toString() + deck.dealCard().toString();
        String playerTwo = deck.dealCard().toString() + deck.dealCard().toString();
        if (parseCards(playerOne).size() != 2 || parseCards(playerTwo).size() != 2) {
            throw new IllegalStateException("Each player must have exactly 2 cards.");
        }
        Board newBoard = new Board(playerOne, playerTwo);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealFlop(Board board) {
        if (deck.size() < 3) {
            throw new IllegalStateException("Not enough cards to deal the flop.");
        }
        String flop = deck.dealCard().toString() + deck.dealCard().toString() + deck.dealCard().toString();
        if (parseCards(flop).size() != 3) {
            throw new IllegalStateException("Each flop must have exactly 3 cards.");
        }
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), flop);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealTurn(Board board) {
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the turn.");
        }
        String turn = deck.dealCard().toString();
        if (parseCards(turn).size() != 1) {
            throw new IllegalStateException("Each turn must have exactly one card.");
        }
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turn);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealRiver(Board board) {
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the river.");
        }
        String river = deck.dealCard().toString();
        if (parseCards(river).size() != 1) {
            throw new IllegalStateException("Each river must have exactly one card.");
        }
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }


    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        if (board.getPlayerOne() == null || parseCards(board.getPlayerOne()).size() != 2 ||
                board.getPlayerTwo() == null || parseCards(board.getPlayerTwo()).size() != 2 ||
                board.getFlop() == null || parseCards(board.getFlop()).size() != 3 ||
                board.getTurn() == null || parseCards(board.getTurn()).size() != 1 || board.getRiver() == null || parseCards(board.getRiver()).size() != 1) {
            throw new InvalidPokerBoardException("Invalid board state: Each player must have exactly 2 cards, and community cards (flop, turn, river) must be fully dealt.");
        }
        PokerValidator.validateBoard(board);
        PokerValidator.printValidationMessageIfNeeded();

        List<Card> playerOneCards = parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = parseCards(board.getPlayerTwo());
        List<Card> communityCards = getCommunityCards(board);

        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);

        printBestHands(playerOneBestHand, playerTwoBestHand);
        PokerResult result = determineWinner(playerOneBestHand, playerTwoBestHand, playerOneCards, playerTwoCards);
        printResult(result);
        return result;
    }

    public PokerResult determineWinner(HandPlayer playerOneHand, HandPlayer playerTwoHand, List<Card> playerOneCards, List<Card> playerTwoCards) {
        int comparison = compareHands(playerOneHand, playerTwoHand, playerOneCards, playerTwoCards);

        if (comparison > 0) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (comparison < 0) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }

    public static int compareHands(HandPlayer hand1, HandPlayer hand2, List<Card> player1Cards, List<Card> player2Cards) {
        // Сравнение приоритетов комбинаций
        int priorityComparison = Integer.compare(hand1.getPriority().getPriority(), hand2.getPriority().getPriority());
        if (priorityComparison != 0) {
            return priorityComparison; // Возвращаем разницу приоритетов
        }

        // Сортируем карты комбинаций по убыванию ранга
        List<Card> sortedHand1 = hand1.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();
        List<Card> sortedHand2 = hand2.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();

        // Сравниваем карты комбинации
        for (int i = 0; i < Math.min(sortedHand1.size(), sortedHand2.size()); i++) {
            int rankComparison = Integer.compare(sortedHand1.get(i).getRankValue(), sortedHand2.get(i).getRankValue());
            if (rankComparison != 0) {
                return rankComparison;
            }
        }
//
//        if (sortedHand1.equals(sortedHand2)) {
//            return 0; // Ничья
//        }
//        // Убираем карты комбинации из общего списка для кикеров
//        Set<Integer> combinationRanksPlayer1 = hand1.getCards().stream()
//                .map(Card::getRankValue)
//                .collect(Collectors.toSet());
//        Set<Integer> combinationRanksPlayer2 = hand2.getCards().stream()
//                .map(Card::getRankValue)
//                .collect(Collectors.toSet());
//        System.out.println("combinationRanksPlayer1:"+combinationRanksPlayer1 + " " + "combinationRanksPlayer2:" +combinationRanksPlayer2);
//        // Если комбинации равны, учитываем кикеры
//        List<Card> kicker1 = sortedHand1.stream()
//                .filter(card -> !combinationRanksPlayer1.contains(card.getRankValue()))
//                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
//                .toList();
//        List<Card> kicker2 = sortedHand2.stream()
//                .filter(card -> !combinationRanksPlayer2.contains(card.getRankValue()))
//                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
//                .toList();
//        System.out.println("Kickers for Player 1: " + kicker1);
//        System.out.println("Kickers for Player 2: " + kicker2);
//        for (int i = 0; i < Math.min(kicker1.size(), kicker2.size()); i++) {
//            int kickerComparison = Integer.compare(kicker1.get(i).getRankValue(), kicker2.get(i).getRankValue());
//            if (kickerComparison != 0) {
//                return kickerComparison;
//            }
//        }
//        // Если все карты равны
        return 0;
    }

    // Метод для сравнения только двух карт игроков


    private void printResult(PokerResult result) {

        switch (result) {
            case PLAYER_ONE_WIN -> System.out.println("Final Result: PLAYER_ONE_WIN");
            case PLAYER_TWO_WIN -> System.out.println("Final Result: PLAYER_TWO_WIN");
            case DRAW -> System.out.println("Final Result: DRAW");
        }

    }

    private List<Card> getCommunityCards(Board board) {
        List<Card> communityCards = parseCards(board.getFlop());
        if (board.getTurn() != null) communityCards.addAll(parseCards(board.getTurn()));
        if (board.getRiver() != null) communityCards.addAll(parseCards(board.getRiver()));
        return communityCards;
    }

    private void printBestHands(HandPlayer playerOneBestHand, HandPlayer playerTwoBestHand) {
        System.out.println("Player One Best Combination: " + playerOneBestHand.getPriority() + " with cards: " + playerOneBestHand.getCards());
        System.out.println("Player Two Best Combination: " + playerTwoBestHand.getPriority() + " with cards: " + playerTwoBestHand.getCards());
    }

    public static List<Card> parseCards(String cardsStr) {
        if (cardsStr == null || cardsStr.isBlank()) {
            throw new IllegalArgumentException("Input string for cards cannot be null or empty.");
        }

        // Удаляем лишние пробелы между картами
        cardsStr = cardsStr.trim().replaceAll("\\s+", "");

        List<Card> cards = new ArrayList<>();
        int i = 0;

        while (i < cardsStr.length()) {
            // Определяем длину текущей карты
            String cardStr;
            if (i + 2 <= cardsStr.length() && cardsStr.substring(i, i + 2).equals("10")) {
                cardStr = cardsStr.substring(i, i + 3); // Берем 3 символа для карты "10"
                i += 3;
            } else if (i + 1 <= cardsStr.length()) {
                cardStr = cardsStr.substring(i, i + 2); // Берем 2 символа для обычной карты
                i += 2;
            } else {
                throw new IllegalArgumentException("Invalid card format in string: " + cardsStr);
            }
            if (cardStr.isEmpty()) {
                throw new IllegalArgumentException("Encountered empty card while parsing: " + cardsStr);
            }
            if (!cardStr.matches("^[2-9TJQKA][CDHS]$|^10[CDHS]$")) {
                throw new IllegalArgumentException("Invalid card format: " + cardStr);
            }
            cards.add(Card.fromString(cardStr));
        }

        return cards;
    }
}

