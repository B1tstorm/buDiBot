package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Analysis.Evaluator;
import de.fh_kiel.discordtradingbot.Analysis.TransactionHistory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;
import discord4j.core.object.entity.channel.MessageChannel;

public class ZuluBot implements EventListener {
    private ChannelInteracter channelInteracter;
    private BuyTransactionManager buyTransactionManager;
    private SegTransactionManager segTransactionManager;
    private SellTransactionManager sellTransactionManager;
    private TransactionHistory transactionHistory;

    public ZuluBot() {
    }

    public void launch() {
        this.channelInteracter = new ChannelInteracter(Config.getToken(), this);
        channelInteracter.listenToChannel();

        this.transactionHistory = TransactionHistory.getInstance();
        this.buyTransactionManager = new BuyTransactionManager(this);
        this.sellTransactionManager = new SellTransactionManager(this);
        this.segTransactionManager = new SegTransactionManager(this);

        channelInteracter.subscribe(TransactionHistory.getInstance());
        channelInteracter.subscribe(buyTransactionManager);
        channelInteracter.subscribe(sellTransactionManager);
        channelInteracter.subscribe(segTransactionManager);

        Evaluator evaluator = Evaluator.getInstance();
        transactionHistory.registerSubscriber(evaluator);
    }

    public ChannelInteracter getChannelInteracter() {
        return channelInteracter;
    }

    public BuyTransactionManager getBuyTransactionManager() {
        return buyTransactionManager;
    }

    public SegTransactionManager getSegTransactionManager() {
        return segTransactionManager;
    }

    public SellTransactionManager getSellTransactionManager() {
        return sellTransactionManager;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public void update(EventItem eventItem) {
        if(eventItem.getEventType() != EventType.HELP) return;
        provideHelp(eventItem.getChannel());

    }

    private void provideHelp(MessageChannel channel) {
        StringBuilder sb = new StringBuilder()
                .append("Moin, ich bin Zulu \n")
                .append("Interagiere mit mir durch folgende Befehle \n")
                .append("Kaufe verfügbare Worte \n")
                .append("Pattern: !ZULU wtb <String> [Preis]\n")
                .append("Beispiel 1: !ZULU wtb HALLO 50\n")
                .append("Beispiel 2: !ZULU wtb HALLO \n")
                .append("Bestätige ein Angebot \n")
                .append("Pattern: !ZULU confirm <ID> \n")
                .append("Beispiel 3: !ZULU confirm 01 \n")
                .append("Lehne ein Angebot ab\n")
                .append("Pattern: !ZULU deny <ID> \n")
                .append("Beispiel 4: !ZULU deny 01 \n");
        channelInteracter.writeThisMessage(sb.toString(), channel);
    }
}