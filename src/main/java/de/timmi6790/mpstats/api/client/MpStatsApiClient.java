package de.timmi6790.mpstats.api.client;

import de.timmi6790.mpstats.api.client.bedrock.BedrockMpStatsApiClient;
import de.timmi6790.mpstats.api.client.java.JavaMpStatsApiClient;
import de.timmi6790.mpstats.api.client.models.filter.Reason;
import lombok.Getter;

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


    public static void main(final String[] args) {
        final MpStatsApiClient apiClient = new MpStatsApiClient(null);

        System.out.println("------------ Game -----------------");
        System.out.println(apiClient.getJavaClient().getGames());
        System.out.println(apiClient.getJavaClient().getGame("Global"));
        System.out.println("------------------------------------");

        System.out.println("------------ Stat -----------------");
        System.out.println(apiClient.getJavaClient().getStats());
        System.out.println(apiClient.getJavaClient().getStat("Wins"));
        System.out.println("------------------------------------");

        System.out.println("------------ Board -----------------");
        System.out.println(apiClient.getJavaClient().getBoards());
        System.out.println(apiClient.getJavaClient().getBoard("All"));
        System.out.println("------------------------------------");

        System.out.println("------------ Leaderboard -----------------");
        System.out.println(apiClient.getJavaClient().getLeaderboards().size());
        System.out.println(apiClient.getJavaClient().getLeaderboard("Global", "ExpEarned", "All"));
        System.out.println("------------------------------------");

        System.out.println("------------ PlayerStats -----------------");
        System.out.println(apiClient.getJavaClient().getPlayerGameStats(
                "nwang888",
                "Global",
                "All",
                EnumSet.noneOf(Reason.class)
        ));
        System.out.println("------------------------------------");
    }
}
