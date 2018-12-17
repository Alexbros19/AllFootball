package com.alexbros.opidlubnyi.allfootball.models;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    public static final long LAST_SEEN_UNKNOWN = 0;
    public static final long LAST_SEEN_ONLINE = -1;

    //    public static final String ATTRIBUTE_KEY_BADGES = "BADGES";
    public static final String ATTRIBUTE_KEY_FAVORITE_TEAMS = "FAVORITE_TEAMS_V2";
    public static final String ATTRIBUTE_KEY_FAVORITE_PLAYERS = "FAVORITE_PLAYERS";
    public static final String ATTRIBUTE_KEY_FOLLOWING_USERS = "FOLLOWING_USERS";
    public static final String ATTRIBUTE_KEY_FOLLOWERS = "FOLLOWERS_USERS";
    //    public static final String ATTRIBUTE_KEY_MY_SELECTION = "FILTER_ITEMS";
//    public static final String ATTRIBUTE_KEY_FB_LINK = "FB_LINK";
//    public static final String ATTRIBUTE_KEY_GP_ACCOUNT_NAME = "GP_ACCOUNT_NAME";
    public static final String ATTRIBUTE_KEY_FAVORITE_LEAGUES = "FAVORITE_LEAGUES_V2";
    public static final int STATUS_TYPE_DIAMOND = 5;
    public static final int STATUS_TYPE_PLATINUM = 4;
    public static final int STATUS_TYPE_GOLD = 3;
    public static final int STATUS_TYPE_SILVER = 2;
    public static final int STATUS_TYPE_BRONZE = 1;
    public static final int STATUS_TYPE_NONE = 0;
    public boolean isMe = false;
    //public boolean isMod = false;
    //public boolean isExpert = false;
    public boolean showAds = true;
    public String userId = "";
    public String userName = "";
    public String followersCount = "";
    public String country = "";
    public String points = "";
    public String pointsUntilNext = "";
    public int status = STATUS_TYPE_NONE;
    public String predictions = "";
    public String predictionsWon = "";
    public String predictionsAccuracy = "";
    public List<League> favoriteLeaguesList = new ArrayList<>();
    public List<UserProfile> followingUsersList = new ArrayList<>();
    public List<UserProfile> followersList = new ArrayList<>();
//    public List<FavoriteTeam> favTeamList = new ArrayList<>();
//    public List<PlayerProfile> favoritePlayersList = new ArrayList<>();
    //    public String followingUsersUrlString = "";
    public boolean isModerator = false;
    public String profilePicture = "";
    public boolean isTVScheduleMaintainer = false;
    public boolean hasProfilePicture = false;
    public List<String> badges = new ArrayList<>();

    public long lastSeen = LAST_SEEN_UNKNOWN;
    public String lastSeenEventId = "";

    public int pendingPredictions = 0;

    public String deviceType = "0";
    public Long notificationDefaults = 0l;
    public ArrayList<String> watchList = new ArrayList<>();

    public String getFavoriteLeaguesAsString() {
        String result = "";
        for (League league : favoriteLeaguesList) {
            result = result + league.id + ";" + league.name + ";";
        }
        return result;
    }

//    public String getFavoritePlayersAsString() {
//        String result = "";
//        for (PlayerProfile player : favoritePlayersList) {
//            result = result + player.id + ";" + player.name + ";";
//        }
//        return result;
//    }

//    public String getFavoriteTeamsAsString() {
//        String result = "";
//        for (FavoriteTeam team : favTeamList) {
//            result = result + team.id + ";" + team.name + ";";
//        }
//        return result;
//    }

    public String getFollowingAsString() {
        String result = "";
        for (UserProfile user : followingUsersList) {
            result = result + user.userId + ";" + user.userName + ";";
        }
        return result;
    }

    public boolean isLeagueFavorite(String id) {
        for (League league : favoriteLeaguesList) {
            if (league.id.equals(id))
                return true;
        }
        return false;
    }

//    public boolean isPlayerFavorite(String id) {
//        for (PlayerProfile player : favoritePlayersList) {
//            if (player.id.equals(id))
//                return true;
//        }
//        return false;
//    }

    public boolean isFollowingUsersContains(String id) {
        for (UserProfile user : followingUsersList) {
            if (user.userId.equals(id)) {
                return true;
            }
        }
        return false;
    }

//    public boolean isTeamFavorite(String id) {
//        for (FavoriteTeam team : favTeamList) {
//            if (team.id.equals(id))
//                return true;
//        }
//        return false;
//    }
//
//    public boolean isTeamFavoriteByName(String name) {
//        for (FavoriteTeam team : favTeamList) {
//            if (team.name.equals(name))
//                return true;
//        }
//        return false;
//    }
//
//    public void removeFavoritePlayer(String id) {
//        for (PlayerProfile player : favoritePlayersList) {
//            if (player.id.equals(id)) {
//                favoritePlayersList.remove(player);
//                return;
//            }
//        }
//    }
//
//    public void removeFavoriteTeam(String id) {
//        for (FavoriteTeam team : favTeamList) {
//            if (team.id.equals(id)) {
//                favTeamList.remove(team);
//                return;
//            }
//        }
//    }

//    public void addFavoriteLeague(League league) {
//        ModelData model = ModelData.getInstance();
//
//        Integer maxOrder = -1;
//
//        for (Integer order : model.favouriteLeaguesOrder.values())
//            if (order.compareTo(maxOrder) > 0)
//                maxOrder = order;
//
//        maxOrder++;
//
//        model.favouriteLeaguesOrder.put(league.id, maxOrder);
//
//        favoriteLeaguesList.add(league);
//    }
//
//    public void removeFavoriteLeague(String id) {
//        ModelData model = ModelData.getInstance();
//        model.favouriteLeaguesOrder.remove(id);
//
//        for (League league : favoriteLeaguesList) {
//            if (league.id.equals(id)) {
//                favoriteLeaguesList.remove(league);
//                return;
//            }
//        }
//    }
//
//    public void removeFollowingUser(String id) {
//        for (UserProfile user : followingUsersList) {
//            if (user.userId.equals(id)) {
//                followingUsersList.remove(user);
//                return;
//            }
//        }
//    }
}
