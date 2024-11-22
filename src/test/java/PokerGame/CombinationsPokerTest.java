package PokerGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//Черви (Hearts)
//
//Красные масти, символизирующие сердце.
//Бубны (Diamonds)
//
//Красные масти, представляющие форму ромба.
//Трефы (Clubs)
//
//Черные масти, напоминающие клеверный лист.
//Пики (Spades)
//
//Черные масти, напоминающие форму наконечника копья.
public class CombinationsPokerTest {
    private Dealer dealer;

    @BeforeEach
    public void setUp() {
        dealer = new DealerDeal(); // Инициализируем объект Dealer
    }
    @Test
    public void testEvaluateBestHand() {
        List<Card> playerCards = Arrays.asList(
                new Card(Rank.ACE, Suit.H),
                new Card(Rank.KING, Suit.H)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.H),
                new Card(Rank.JACK, Suit.H),
                new Card(Rank.TEN, Suit.H),
                new Card(Rank.TWO, Suit.C),
                new Card(Rank.THREE, Suit.S)
        );

        HandPlayer bestHand = PokerHandEvaluator.evaluateBestHand(playerCards, communityCards);
        System.out.println("Best hand: " + bestHand);
        assertEquals(Priority.ROYAL_FLUSH, bestHand.getPriority());
    }
    @Test
    public void testHighCard() {
        Board board = new Board(
                "AH5D", // Player One
                "QS8C", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с AHKH
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testOnePair() {
        Board board = new Board(
                "AD5H", // Player One
                "QS8C", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        System.out.println("Result: " + result);// Побеждает игрок со старшей кикер-картой AC
    }
    //Черви (Hearts)
    //Бубны (Diamonds)
    //Трефы (Clubs)
    //Пики (Spades)
    @Test
    public void testTwoPair() {
        Board board = new Board(
                "7D6D", // Player One
                "6H5H", // Player Two
                "7C5C4S", // Flop
                "6S", // Turn
                "KH" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с KHQH
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testThreeOfAKind() {
        Board board = new Board(
                "AHAC", // Player One
                "QSQD", // Player Two
                "QH8CAS", // Flop
                "KD", // Turn
                "4C" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с 3S3C и старшей кикер-картой KH
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testStraight() {
        Board board = new Board(
                "9SJS", // Player One
                "9C10D", // Player Two
                "5S6H7C", // Flop
                "8D", // Turn
                "10S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей картой Straight 7C
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFlush() {
        Board board = new Board(
                "ASKS", // Player One
                "3S7S", // Player Two
                "3D3H10S", // Flop
                "AS", // Turn
                "4S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с Flush AH10H
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFullHouse() {
        Board board = new Board(
                "9C9H", // Player One
                "6C6S", // Player Two
                "2C2D2S", // Flop
                "QS", // Turn
                "JD" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей парой 6C6H
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFourOfAKind() {
        Board board = new Board(
                "8C8H", // Player One
                "QDQC", // Player Two
                "8DQH4C", // Flop
                "9H", // Turn
                "8S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с кикером AS
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testStraightFlush() {
        Board board = new Board(
                "JC10C", // Player One
                "6C5C", // Player Two
                "3C7C8C", // Flop
                "9C", // Turn
                "AC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей картой 8H9H
    }
    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testRoyalFlush() {
        Board board = new Board(
                "AH4H", // Player One
                "2H9H", // Player Two
                "5H10HQH", // Flop
                "KH", // Turn
                "JH" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с AHKH
    }
}

