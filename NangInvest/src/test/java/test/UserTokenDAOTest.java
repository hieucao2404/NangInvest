package test;

import dao.UserTokenDAO;
import model.UserToken;
import util.JPAUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Test class for UserTokenDAO
 * Tests CRUD operations and custom queries for UserToken
 */
public class UserTokenDAOTest {

    private static UserTokenDAO userTokenDAO;

    public static void main(String[] args) {
        System.out.println("=== Starting UserTokenDAO Test ===");
        
        try {
            // Initialize DAO
            userTokenDAO = new UserTokenDAO();
            
            // Run all tests
            testCRUDOperations();
            testCustomQueries();
            testUtilityMethods();
            testEdgeCases();
            
            System.out.println("=== All UserTokenDAO tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAUtil.closeEntityManagerFactory();
        }
    }

    private static void testCRUDOperations() {
        System.out.println("\n--- Testing CRUD Operations ---");

        // Test Create
        UserToken token1 = new UserToken();
        token1.setUserId(1);
        token1.setToken("token_user1_active");
        token1.setExpiryDate(LocalDateTime.now().plusDays(30));

        UserToken savedToken1 = userTokenDAO.save(token1);
        System.out.println("Created user token: User " + savedToken1.getUserId() + 
                          ", Token " + savedToken1.getToken() + 
                          " (ID: " + savedToken1.getId() + ")");

        UserToken token2 = new UserToken();
        token2.setUserId(1);
        token2.setToken("token_user1_expired");
        token2.setExpiryDate(LocalDateTime.now().minusDays(1));

        UserToken savedToken2 = userTokenDAO.save(token2);
        System.out.println("Created user token: User " + savedToken2.getUserId() + 
                          ", Token " + savedToken2.getToken() + 
                          " (ID: " + savedToken2.getId() + ")");

        UserToken token3 = new UserToken();
        token3.setUserId(2);
        token3.setToken("token_user2_active");
        token3.setExpiryDate(LocalDateTime.now().plusDays(15));

        UserToken savedToken3 = userTokenDAO.save(token3);
        System.out.println("Created user token: User " + savedToken3.getUserId() + 
                          ", Token " + savedToken3.getToken() + 
                          " (ID: " + savedToken3.getId() + ")");

        // Test Read
        UserToken foundToken = userTokenDAO.findById(savedToken1.getId()).orElse(null);
        System.out.println("Found token by ID: " + 
                          (foundToken != null ? "User " + foundToken.getUserId() + ", Token " + foundToken.getToken() : "Not found"));

        // Test Update
        savedToken1.setExpiryDate(LocalDateTime.now().plusDays(60));
        UserToken updatedToken = userTokenDAO.update(savedToken1);
        System.out.println("Updated token expiry date: " + updatedToken.getExpiryDate());

        // Test findAll
        List<UserToken> allTokens = userTokenDAO.findAll();
        System.out.println("Total user tokens in database: " + allTokens.size());
    }

    private static void testCustomQueries() {
        System.out.println("\n--- Testing Custom Queries ---");

        // Test findByUserId
        List<UserToken> user1Tokens = userTokenDAO.findByUserId(1);
        System.out.println("Tokens for user 1: " + user1Tokens.size());

        List<UserToken> user2Tokens = userTokenDAO.findByUserId(2);
        System.out.println("Tokens for user 2: " + user2Tokens.size());

        // Test findByToken
        Optional<UserToken> specificTokenOpt = userTokenDAO.findByToken("token_user1_active");
        UserToken specificToken = specificTokenOpt.orElse(null);
        System.out.println("Found specific token 'token_user1_active': " + 
                          (specificToken != null ? "User " + specificToken.getUserId() : "Not found"));

        // Test findExpiredTokens
        List<UserToken> expiredTokens = userTokenDAO.findExpiredTokens();
        System.out.println("Expired tokens: " + expiredTokens.size());

        // Test findValidTokensByUserId
        List<UserToken> user1ValidTokens = userTokenDAO.findValidTokensByUserId(1);
        System.out.println("Valid tokens for user 1: " + user1ValidTokens.size());

        // Test isTokenValid
        boolean token1Valid = userTokenDAO.isTokenValid("token_user1_active");
        System.out.println("Token 'token_user1_active' is valid: " + token1Valid);

        boolean token2Valid = userTokenDAO.isTokenValid("token_user1_expired");
        System.out.println("Token 'token_user1_expired' is valid: " + token2Valid);
    }

    private static void testUtilityMethods() {
        System.out.println("\n--- Testing Utility Methods ---");

        // Test getTokenCountByUserId
        long user1TokenCount = userTokenDAO.getTokenCountByUserId(1);
        System.out.println("Token count for user 1: " + user1TokenCount);

        long user2TokenCount = userTokenDAO.getTokenCountByUserId(2);
        System.out.println("Token count for user 2: " + user2TokenCount);

        // Test getValidTokenCountByUserId
        long user1ValidTokenCount = userTokenDAO.getValidTokenCountByUserId(1);
        System.out.println("Valid token count for user 1: " + user1ValidTokenCount);

        long user2ValidTokenCount = userTokenDAO.getValidTokenCountByUserId(2);
        System.out.println("Valid token count for user 2: " + user2ValidTokenCount);

        // Test updateTokenExpiry
        Optional<UserToken> tokenToExtendOpt = userTokenDAO.findByToken("token_user2_active");
        if (tokenToExtendOpt.isPresent()) {
            UserToken tokenToExtend = tokenToExtendOpt.get();
            LocalDateTime newExpiry = LocalDateTime.now().plusDays(90);
            userTokenDAO.updateTokenExpiry(tokenToExtend.getToken(), newExpiry);
            System.out.println("Updated token expiry for 'token_user2_active' to: " + newExpiry);
        }

        // Test findTokensExpiringSoon
        List<UserToken> expiringTokens = userTokenDAO.findTokensExpiringSoon(24 * 45); // 45 days in hours
        System.out.println("Tokens expiring within 45 days: " + expiringTokens.size());

        // Test createOrUpdateToken
        String newTokenValue = "generated_token_" + System.currentTimeMillis();
        LocalDateTime expiry = LocalDateTime.now().plusDays(30);
        UserToken generatedToken = userTokenDAO.createOrUpdateToken(3, newTokenValue, expiry);
        System.out.println("Created/Updated token: " + generatedToken.getToken() + " for user " + generatedToken.getUserId());
    }

    private static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");

        // Test with token having edge case expiry dates
        UserToken futureToken = new UserToken();
        futureToken.setUserId(999);
        futureToken.setToken("far_future_token");
        futureToken.setExpiryDate(LocalDateTime.now().plusYears(10));

        UserToken savedFutureToken = userTokenDAO.save(futureToken);
        System.out.println("Created far future token: " + savedFutureToken.getToken());

        UserToken veryOldToken = new UserToken();
        veryOldToken.setUserId(999);
        veryOldToken.setToken("very_old_token");
        veryOldToken.setExpiryDate(LocalDateTime.now().minusYears(1));

        UserToken savedOldToken = userTokenDAO.save(veryOldToken);
        System.out.println("Created very old token: " + savedOldToken.getToken());

        // Test queries with non-existent data
        List<UserToken> nonExistentUserTokens = userTokenDAO.findByUserId(99999);
        System.out.println("Tokens for non-existent user: " + nonExistentUserTokens.size());

        Optional<UserToken> nonExistentTokenOpt = userTokenDAO.findByToken("non_existent_token");
        UserToken nonExistentToken = nonExistentTokenOpt.orElse(null);
        System.out.println("Non-existent token: " + (nonExistentToken != null ? "Found" : "Not found"));

        // Test isTokenValid with non-existent token
        boolean nonExistentTokenValid = userTokenDAO.isTokenValid("non_existent_token");
        System.out.println("Non-existent token is valid: " + nonExistentTokenValid);

        // Test utility methods with non-existent data
        long nonExistentUserTokenCount = userTokenDAO.getTokenCountByUserId(99999);
        System.out.println("Token count for non-existent user: " + nonExistentUserTokenCount);

        // Test deleteExpiredTokens
        int deletedExpiredCount = userTokenDAO.deleteExpiredTokens();
        System.out.println("Deleted expired tokens: " + deletedExpiredCount);

        // Test deleteTokensByUserId
        int revokedCount = userTokenDAO.deleteTokensByUserId(999);
        System.out.println("Deleted tokens for user 999: " + revokedCount);

        // Test deleteByToken
        boolean revoked = userTokenDAO.deleteByToken("far_future_token");
        System.out.println("Deleted specific token 'far_future_token': " + revoked);

        // Test findTokensExpiringSoon with various values
        List<UserToken> tokensExpiringInZeroHours = userTokenDAO.findTokensExpiringSoon(0);
        System.out.println("Tokens expiring within 0 hours: " + tokensExpiringInZeroHours.size());

        List<UserToken> tokensExpiringInManyHours = userTokenDAO.findTokensExpiringSoon(24 * 3650); // 10 years in hours
        System.out.println("Tokens expiring within 10 years: " + tokensExpiringInManyHours.size());

        // Test count methods
        Long totalCount = userTokenDAO.count();
        System.out.println("Total token count (generic): " + totalCount);

        if (totalCount > 0) {
            List<UserToken> allTokens = userTokenDAO.findAll();
            UserToken firstToken = allTokens.get(0);
            boolean exists = userTokenDAO.existsById(firstToken.getId());
            System.out.println("First token exists by ID: " + exists);
        }
    }
}
