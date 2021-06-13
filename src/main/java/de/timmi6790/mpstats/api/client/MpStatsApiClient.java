package de.timmi6790.mpstats.api.client;

import de.timmi6790.mpstats.api.client.bedrock.BedrockMpStatsApiClient;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerEntry;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;
import de.timmi6790.mpstats.api.client.java.JavaMpStatsApiClient;
import de.timmi6790.mpstats.api.client.java.player.models.JavaPlayer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

@Getter
// TODO: Handle nginx responses for api down
public class MpStatsApiClient {
    private final JavaMpStatsApiClient javaClient;
    private final BedrockMpStatsApiClient bedrockClient;

    public MpStatsApiClient(final String apiKey) {
        this("https://mpstats2.timmi6790.de", apiKey);
    }

    public MpStatsApiClient(final String baseUrl, final String apiKey) {
        this.javaClient = new JavaMpStatsApiClient(baseUrl, apiKey);
        this.bedrockClient = new BedrockMpStatsApiClient(baseUrl, apiKey);
    }

    @SneakyThrows
    public static void main(final String[] args) {
        // Btw, don't even try this key for the production api
        final MpStatsApiClient apiClient = new MpStatsApiClient("http://127.0.0.1:8080", null);

        System.out.println("------------ Java Group -----------------");
        System.out.println(apiClient.getJavaClient().getGroupClient().getGroup("Arcade"));
        System.out.println(apiClient.getJavaClient().getGroupClient().getGroups());
        System.out.println(apiClient.getJavaClient().getGroupClient().getPlayerStats(
                "Alien",
                "Timmi6790",
                "E",
                "A",
                ZonedDateTime.now(),
                EnumSet.noneOf(Reason.class),
                false
        ));
        System.out.println("------------------------------------");

        System.out.println("------------ Bedrock PlayerStats -----------------");
        System.out.println(apiClient.getBedrockClient().getPlayerClient().getPlayerStats("usniic", false, EnumSet.noneOf(Reason.class)));
        System.out.println("------------------------------------");

        System.out.println("------------ Game -----------------");
        System.out.println(apiClient.getJavaClient().getGameClient().getGames().size());
        System.out.println(apiClient.getJavaClient().getGameClient().getGame("Global"));
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
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboards("Global"));
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboards("Global", "ExpEarned"));
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboard("Global", "ExpEarned", "All"));
        System.out.println(apiClient.getJavaClient().getLeaderboardClient().getLeaderboardSave("Global", "ExpEarned", "All", EnumSet.noneOf(Reason.class)).isPresent());
        System.out.println("------------------------------------");

        System.out.println("------------ PlayerStats -----------------");
        System.out.println(apiClient.getJavaClient().getPlayerClient().getPlayerGameStats(
                "Timmi6790",
                "SSM2",
                "All",
                false,
                EnumSet.noneOf(Reason.class)
        ));
        System.out.println(apiClient.getJavaClient().getPlayerClient().getPlayerGameStats(
                UUID.fromString("9d59daad-6f62-4bd9-b13e-c961bf906750"),
                "SSM2",
                "All",
                false,
                EnumSet.noneOf(Reason.class)
        ));
        System.out.println(apiClient.getJavaClient().getPlayerClient().getPlayerStatStats(
                UUID.fromString("9d59daad-6f62-4bd9-b13e-c961bf906750"),
                "Wins",
                "All",
                false,
                EnumSet.noneOf(Reason.class)
        ));

        final Optional<PlayerStats<JavaPlayer>> d = apiClient.getJavaClient().getPlayerClient().getPlayerGameStats(
                "Timmi6790",
                "SSM2",
                "All",
                false,
                EnumSet.noneOf(Reason.class)
        );

        for (final PlayerEntry entry : d.get().getStats()) {
            System.out.println(entry.getLeaderboard().getStat());
        }
        System.out.println("------------------------------------");
    }
}
