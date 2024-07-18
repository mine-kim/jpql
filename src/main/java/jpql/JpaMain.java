package jpql;

import org.h2.value.Typed;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < 20; i++) {
                Product product = new Product();
                product.setName("pro"+i);
                product.setPrice(i);
                product.setStockAmount(i);
                em.persist(product);
            }
            Product product20 = new Product();
            product20.setName("pro"+20);
            product20.setPrice(20);
            product20.setStockAmount(20);
            em.persist(product20);

            String qlString = "update Product p " +
                    "set p.price = p.price * 3 " +
                    "where p.stockAmount < :stockAmount";
            int resultCount = em.createQuery(qlString)
                    .setParameter("stockAmount", 30)
                    .executeUpdate();

            // 영속성 컨텍스트 클리어. 업데이트 결과를 다시 DB에서 조회하도록.
            em.flush();
            em.clear();

            Product product = em.find(Product.class, product20.getId());
            System.out.println("product = " + product.getPrice());

            tx.commit();

          /*  Team team1 = new Team();
            team1.setName("team1");
            Team team2 = new Team();
            team2.setName("team2");
            em.persist(team1);
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(team1);
            member1.setType(MemberType.USER);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.setTeam(team1);
            member2.setType(MemberType.USER);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(10);
            member3.setTeam(team2);
            member3.setType(MemberType.USER);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            String jpql1 = "select m from Member m join fetch m.team";
            List<Member> members = em.createQuery(jpql1, Member.class)
                    .getResultList();
            for (Member member : members) {
                // 페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩X
                System.out.println("username = " + member.getUsername() + ", " + "teamName = " + member.getTeam().getName());
            }

            String jpql2 = "select distinct t from Team t join fetch t.members";
            List<Team> teams = em.createQuery(jpql2, Team.class).getResultList();
            for(Team team : teams) {
                System.out.println("team = " + team + ", members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    //페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함
                    System.out.println("-> username = " + member.getUsername()+ ", member = " + member);
                }
            }





          for (int i = 0; i < 100 ; i++) {
                Member member = new Member();
                member.setUsername("member"+i);
                member.setAge(i);
                member.setType(MemberType.USER);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> list = em.createQuery("select m from Member m", Member.class)
                    .getResultList(); // 결과 없으면 빈 리스트 반환

            List<MemberDTO> members = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            List<Member> members2 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            for(Member member2 : members2) {
                System.out.println("member = " + member2.toString());
            }

            List<Object[]> result = em.createQuery("select m.username, 'HELLO', true From Member m where m.type = :userType")
                    .setParameter("userType", MemberType.USER)
                    .getResultList();

            for(Object[] o : result) {
                System.out.println("Object = " + o[0]);
                System.out.println("Object = " + o[1]);
                System.out.println("Object = " + o[2]);
            }

            em.createQuery("select case when m.age <= 10 then '학생요금' when m.age >= 60 then '경로요금' else '일반요금' end from Member m");
            em.createQuery("select function('group_concat', m.username) from Member m").getResultList();*/


        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
