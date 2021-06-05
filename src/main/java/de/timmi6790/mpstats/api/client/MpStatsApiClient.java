package de.timmi6790.mpstats.api.client;

import de.timmi6790.mpstats.api.client.bedrock.BedrockMpStatsApiClient;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.java.JavaMpStatsApiClient;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.EnumSet;

@Getter
public class MpStatsApiClient {
    private final JavaMpStatsApiClient javaClient;
    private final BedrockMpStatsApiClient bedrockClient;

    public MpStatsApiClient(final String apiKey) {
        this("http://127.0.0.1:8080", apiKey);
    }

    public MpStatsApiClient(final String baseUrl, final String apiKey) {
        this.javaClient = new JavaMpStatsApiClient(baseUrl, apiKey);
        this.bedrockClient = new BedrockMpStatsApiClient(baseUrl, apiKey);
    }

    @SneakyThrows
    public static void main(final String[] args) {
        final MpStatsApiClient apiClient = new MpStatsApiClient(null);

        System.out.println("------------ Bedrock PlayerStats -----------------");
        System.out.println(apiClient.getBedrockClient().getPlayerClient().getPlayerStats("usniic", EnumSet.noneOf(Reason.class)));
        System.out.println("------------------------------------");

        System.out.println("------------ Game -----------------");
        System.out.println(apiClient.getJavaClient().getGameClient().getGames().size());
        System.out.println(apiClient.getJavaClient().getGameClient().getGame("Global"));
        System.out.println(apiClient.getJavaClient().getGameClient().getGame("Globald"));
        System.out.println("------------------------------------");

        System.out.println("------------ Stat -----------------");
        System.out.println(apiClient.getJavaClient().getStatClient().getStats().size());
        System.out.println(apiClient.getJavaClient().getStatClient().getStat("Wins"));
        System.out.println("------------------------------------");

        System.out.println("------------ Board -----------------");
        System.out.println(apiClient.getJavaClient().getBoardClient().getBoards().size());
        System.out.println(apiClient.getJavaClient().getBoardClient().getBoard("All"));
        System.out.println("------------------------------------");

        System.out.println("------------ Leaderboard -----------------");
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboards().size());
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboard("Global", "ExpEarned", "All"));
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboardSave("Global", "ExpEarned", "All", EnumSet.noneOf(Reason.class)).isPresent());
        System.out.println("------------------------------------");

        System.out.println("------------ PlayerStats -----------------");
        System.out.println(apiClient.getJavaClient().getPlayerClient().getPlayerGameStats(
                "nwang888",
                "Global",
                "All",
                EnumSet.noneOf(Reason.class)
        ));
        System.out.println("------------------------------------");
    }
}
