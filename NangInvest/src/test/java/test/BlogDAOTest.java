package test;

import java.util.List;
import java.util.Optional;

import dao.BlogDAO;
import model.Blog;

/**
 * Test class for BlogDAO functionality
 */
public class BlogDAOTest {

  public static void main(String[] args) {
    BlogDAO blogDAO = new BlogDAO();

    System.out.println("🧪 Testing BlogDAO functionality...\n");

    try {
      // Test 1: Basic CRUD Operations
      System.out.println("1️⃣ Testing basic CRUD operations:");

      // Create a new blog
      Blog testBlog = new Blog(
          "Investment Strategies for 2025",
          "Finance",
          "https://example.com/blog-image.jpg",
          "This blog post discusses various investment strategies that will be effective in 2025. It covers topics like diversification, risk management, and emerging markets.");
      Blog savedBlog = blogDAO.save(testBlog);
      System.out.println("✅ Created blog: " + savedBlog);

      // Read the blog
      Optional<Blog> foundBlog = blogDAO.findById(savedBlog.getBlogId());
      System.out.println("✅ Found blog: " + foundBlog.orElse(null));

      // Update the blog
      if (foundBlog.isPresent()) {
        Blog blog = foundBlog.get();
        blog.setTopic("Investment");
        Blog updatedBlog = blogDAO.update(blog);
        System.out.println("✅ Updated blog: " + updatedBlog);
      }

      System.out.println();

      // Test 2: Custom finder methods
      System.out.println("2️⃣ Testing custom finder methods:");

      // Find by blog name
      Optional<Blog> blogByName = blogDAO.findByBlogName("Investment Strategies for 2025");
      System.out.println("✅ Found by name: " + blogByName.orElse(null));

      // Find blogs by topic
      List<Blog> investmentBlogs = blogDAO.findByTopic("Investment");
      System.out.println("✅ Investment blogs count: " + investmentBlogs.size());

      // Find blogs with images
      List<Blog> blogsWithImages = blogDAO.findBlogsWithImages();
      System.out.println("✅ Blogs with images count: " + blogsWithImages.size());

      // Check if blog name exists
      boolean exists = blogDAO.existsByBlogName("Investment Strategies for 2025");
      System.out.println("✅ Blog name exists: " + exists);

      System.out.println();

      // Test 3: Content search
      System.out.println("3️⃣ Testing content search:");

      List<Blog> blogsWithKeyword = blogDAO.findByContentContaining("investment");
      System.out.println("✅ Blogs containing 'investment': " + blogsWithKeyword.size());

      List<Blog> blogsByPartialName = blogDAO.findByBlogNameContaining("Investment");
      System.out.println("✅ Blogs with 'Investment' in name: " + blogsByPartialName.size());

      System.out.println();

      // Test 4: Topic management
      System.out.println("4️⃣ Testing topic management:");

      List<String> allTopics = blogDAO.findAllTopics();
      System.out.println("✅ All unique topics: " + allTopics);

      long blogCountByTopic = blogDAO.getBlogCountByTopic("Investment");
      System.out.println("✅ Investment topic blog count: " + blogCountByTopic);

      System.out.println();

      // Test 5: Statistical queries
      System.out.println("5️⃣ Testing statistical queries:");

      long totalBlogCount = blogDAO.getBlogCount();
      System.out.println("✅ Total blog count: " + totalBlogCount);

      System.out.println();

      // Test 6: Pagination
      System.out.println("6️⃣ Testing pagination:");

      List<Blog> paginatedBlogs = blogDAO.findBlogsPaginated(0, 5);
      System.out.println("✅ First 5 blogs: " + paginatedBlogs.size());

      List<Blog> topicPaginated = blogDAO.findBlogsByTopicPaginated("Investment", 0, 3);
      System.out.println("✅ First 3 Investment blogs: " + topicPaginated.size());

      System.out.println();

      // Test 7: Update operations
      System.out.println("7️⃣ Testing update operations:");

      if (savedBlog != null) {
        blogDAO.updateBlogImage(savedBlog.getBlogId(), "https://example.com/updated-blog-image.jpg");
        System.out.println("✅ Updated blog image URL");

        blogDAO.updateBlogContent(savedBlog.getBlogId(),
            "Updated content: This blog post has been revised with the latest investment strategies for 2025.");
        System.out.println("✅ Updated blog content");

        // Verify the updates
        Optional<Blog> verifyBlog = blogDAO.findById(savedBlog.getBlogId());
        if (verifyBlog.isPresent()) {
          Blog blog = verifyBlog.get();
          System.out.println("✅ Verified image URL: " + blog.getImageUrl());
          System.out.println("✅ Verified content preview: " + blog.getPreviewContent(50));
        }
      }

      System.out.println();

      // Test 8: Utility methods
      System.out.println("8️⃣ Testing utility methods:");

      if (foundBlog.isPresent()) {
        Blog blog = foundBlog.get();
        System.out.println("✅ Has image: " + blog.hasImage());
        System.out.println("✅ Preview content (100 chars): " + blog.getPreviewContent(100));
      }

      System.out.println();

      // Test 9: Cleanup - Delete the test blog
      System.out.println("9️⃣ Testing delete operations:");

      if (savedBlog != null) {
        blogDAO.delete(savedBlog);
        System.out.println("✅ Deleted test blog");

        // Verify deletion
        Optional<Blog> deletedBlog = blogDAO.findById(savedBlog.getBlogId());
        System.out.println(
            "✅ Blog after deletion: " + (deletedBlog.isEmpty() ? "Not found (correct)" : "Still exists (error)"));
      }

      System.out.println("\n🎉 All BlogDAO tests completed successfully!");

    } catch (Exception e) {
      System.err.println("❌ Error during testing: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
