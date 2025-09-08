package dev.chearcode.filter;

import dev.chearcode.config.ContextAttribute;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;

@WebFilter("/*")
public class TransactionManager extends HttpFilter {
    private SessionFactory sessionFactory;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.sessionFactory = (SessionFactory) getServletContext().getAttribute(ContextAttribute.SESSION_FACTORY);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            chain.doFilter(req, res);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
