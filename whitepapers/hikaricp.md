# Maximizing HikariCP Performance in Spring Boot Applications

## Summary
HikariCP is a lightweight and high-performing JDBC connection pool used in Spring Boot applications. Below are key considerations and best practices for optimizing HikariCP performance.

---

## Key Points

### 1. **HikariCP Configuration Properties**
   - **`spring.datasource.hikari.maximum-pool-size`**: Controls the maximum number of connections in the pool.
   - **`spring.datasource.hikari.minimum-idle`**: Sets the minimum number of idle connections in the pool.
   - **`spring.datasource.hikari.connection-timeout`**: Defines the maximum wait time for a connection from the pool before throwing an exception.
   - **`spring.datasource.hikari.idle-timeout`**: Specifies how long a connection can be idle before being removed from the pool.
   - **`spring.datasource.hikari.max-lifetime`**: Sets the maximum lifetime of a connection in the pool.

### 2. **Optimizing Pool Sizing**
   - Use the formula: 
     
     ```
     Optimal Pool Size = ((Core Count * 2) + Effective Spindle Count)
     ```
   - Evaluate based on application workload and database responsiveness.

### 3. **Connection Leak Detection**
   - Enable leak detection using:
     
     ```properties
     spring.datasource.hikari.leak-detection-threshold=2000
     ```
   - Logs any connection in use longer than the specified threshold (in milliseconds).

### 4. **Reducing Connection Timeouts**
   - Keep `connection-timeout` as low as practical to ensure connections return to the pool quickly.
   
   ```properties
   spring.datasource.hikari.connection-timeout=30000
   ```

### 5. **Connection Validation**
   - Use a lightweight validation query or let HikariCP validate connections with its efficient internal mechanism:
     
     ```properties
     spring.datasource.hikari.validation-timeout=5000
     ```

### 6. **Housekeeping Settings**
   - **`spring.datasource.hikari.idle-timeout`**: Recommended to set slightly lower than database timeout settings to avoid premature connection termination.
   - **`spring.datasource.hikari.max-lifetime`**: Use this to prevent stale connections by setting a value slightly below database connection time limits.
   - HikariCP performs housekeeping tasks asynchronously, ensuring optimal performance and minimal overhead.

### 7. **Database-Specific Adjustments**
   - Tailor settings such as maximum pool size, idle timeout, and validation queries to your database system (PostgreSQL, MySQL, etc.).

### 8. **Monitoring and Metrics**
   - Leverage HikariCP metrics with Micrometer to monitor connection pool health.
   - Example Spring Boot setup:
     ```groovy
     implementation 'io.micrometer:micrometer-core'
     implementation 'io.micrometer:micrometer-registry-prometheus'
     ```

---

## Spring Logging for HikariCP

1. **Enable DEBUG Logs:**
   - Add the following configuration in `application.properties` or `application.yml`:
     
     ```properties
     logging.level.com.zaxxer.hikari=DEBUG
     logging.level.org.springframework.jdbc.datasource=DEBUG
     ```
   - Provides detailed logs about connection pool behavior and SQL execution.

2. **Monitor Connection Usage:**
   - Spring Boot logs connection usage metrics to help diagnose performance issues.

3. **Log Outputs:**
   - Check logs for lifecycle events like connection acquisition, validation, or leak detection alerts.

---

## Best Practices

1. **Avoid Over-Tuning:** Begin with default settings and incrementally adjust based on profiling data.
2. **Monitor Regularly:** Use monitoring tools like Prometheus or Grafana for real-time insights.
3. **Test with Load:** Simulate production workloads to validate pool configurations.
4. **Use Read Replicas:** For read-heavy workloads, distribute queries across read replicas.
5. **Upgrade Dependencies:** Ensure you're using the latest stable version of HikariCP for bug fixes and performance improvements.

---

## References
- [Maximizing HikariCP Performance in Spring Boot Applications](https://medium.com/@mukitulislamratul/maximizing-hikaricp-performance-in-spring-boot-applications-f7ee8474410a)
- [HikariCP Official Documentation](https://github.com/brettwooldridge/HikariCP)
- [Spring Boot HikariCP Configuration](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.sql.datasource.hikari)

---
