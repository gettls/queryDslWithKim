package study.querydsl.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.ldap.DataLdapTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;


@SpringBootTest
@Transactional
@Commit
class MemberTest {

	@Autowired
	EntityManager em;
	
	
	private JPAQueryFactory queryFactory = new JPAQueryFactory(em);
	
	@Test
	void test() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		
		em.persist(teamA);
		em.persist(teamB);
		
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
		
		// �ʱ�ȭ
		em.flush();
		em.clear();
		
		// Ȯ��
		List<Member> members = em.createQuery("select m from Member m",Member.class)
									.getResultList();
		
		for(Member member : members) {
			System.out.println("member = "+member);
			System.out.println("-> member.team = "+member.getTeam());
		}
	}
	
	@Test
	public void startJPQL() {
		// member 1 �� ã�ƶ�
		Member findByJPQL = em.createQuery("select m from Member m where m.username = :username", Member.class)
			.setParameter("username", "member1")
			.getSingleResult();
		
		Assertions.assertThat(findByJPQL.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void startQuerydsl() {
		
		Member findMember = queryFactory
					.select(member)
					.from(member)
					.where(member.username.eq("member1"))
					.fetchOne();
		
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void search() {
		Member findOne = queryFactory
				.selectFrom(member)
				.where(member.username.eq("member1").and(member.age.eq(10)))
				.fetchOne();
		
		Assertions.assertThat(findOne.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void searchAndParam() {
		Member findOne = queryFactory
				.selectFrom(member)
				.where(
						member.username.eq("member1"),
						member.age.eq(10)
				)
				.fetchOne();
		
		Assertions.assertThat(findOne.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void resultFetch() {
		List<Member> fetch = queryFactory
				.selectFrom(member)
				.fetch();
		
		Member fetchOne = queryFactory
				.selectFrom(member)
				.fetchOne();
		
		Member fetchFirst = queryFactory
				.selectFrom(member)
				.fetchFirst();
		
		List<Member> results = queryFactory
				.selectFrom(member)
				.offset(0)
				.limit(10)
				.fetch();
	}
	
	
	@Test
	public void sort() {
		em.persist(new Member(null, 100));
		em.persist(new Member("member5", 100));
		em.persist(new Member("member6", 100));
		
		List<Member> result = queryFactory
				.selectFrom(member)
				.where(member.age.eq(100))
				.orderBy(member.age.desc(), member.username.asc().nullsLast())
				.fetch();
		
		Member member5 = result.get(0);
		Member member6 = result.get(1);
		Member memberNull = result.get(2);
		
		Assertions.assertThat(member5.getUsername()).isEqualTo("member5");
	}
	
	
	@Test
	public void paging() {
		List<Member> result = queryFactory
				.selectFrom(member)
				.orderBy(member.username.desc())
				.offset(1)
				.limit(2)
				.fetch();
		
		Assertions.assertThat(result.size()).isEqualTo(2);
	}
	
	@Test
	public void aggregation() {
		List<Tuple> result = queryFactory
				.select(
						member.count(),
						member.age.sum(),
						member.age.avg(),
						member.age.max(),
						member.age.min()
				)
				.from(member)
				.fetch();
		
		Tuple tuple = result.get(0);
		Assertions.assertThat(tuple.get(member.count()));
	}
	
	
	/*
	 * �� ���� �̸��� ����� ��� ���̸� ���ض�
	 */
	@Test
	public void group() throws Exception{
		List<Tuple> result = queryFactory
				.select(team.name, member.age.avg())
				.from(member)
				.join(member.team, team)
				.groupBy(team.name)
				.fetch();
		
		Tuple teamA = result.get(0);
		Tuple teamB = result.get(1);
		
		Assertions.assertThat(teamA.get(team.name)).isEqualTo("teamA");
		Assertions.assertThat(teamA.get(member.age.avg())).isEqualTo("15");
		
		Assertions.assertThat(teamB.get(team.name)).isEqualTo("teamB");
		Assertions.assertThat(teamB.get(member.age.avg())).isEqualTo("35");
	}
	
	
	/*
	 * �� A�� �Ҽӵ� ��� ȸ��
	 */
	@Test
	public void join() {
		List<Member> result = queryFactory
				.selectFrom(member)
				.join(member.team, team)
				.where(team.name.eq("teamA"))
				.fetch();
		
		Assertions.assertThat(result)
				.extracting("username")
				.containsExactly("member1","member2");
	}
	
	/*
	 * ��Ÿ ����
	 * ȸ���� �̸��� �� �̸��� ���� ȸ�� ��ȸ
	 */
	@Test
	public void theta_join() {
		em.persist(new Member("teamA"));
		em.persist(new Member("teamB"));
		
		List<Member> result = queryFactory
				.select(member)
				.from(member, team)
				.where(member.username.eq(team.name))
				.fetch();
		
		Assertions.assertThat(result)
					.extracting("username")
					.containsExactly("teamA","teamB");
	}
	
	/*
	 * ��) ȸ���� ���� �����ϸ鼭, �� �̸��� teamA�� ���� ����, ȸ���� ��� ��ȸ
	 * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
	 */
	@Test
	public void join_on_filtering() {
		List<Tuple> result = queryFactory
		 		.select(member, team)
				.from(member)
				.leftJoin(member.team, team).on(team.name.eq("teamA"))
				.fetch();
	}
	
	
	/*
	 * �������谡 ���� ��ƼƼ �ܺ� ����
	 * ȸ���� �̸��� �� �̸��� ���� ��� �ܺ� ��ȸ
	 */
	@Test
	public void join_on_no_relation() {
		em.persist(new Member("teamA"));
		em.persist(new Member("teamB"));
		
		List<Tuple> result = queryFactory
				.select(member,team)
				.from(member)
				.leftJoin(team).on(member.username.eq(team.name))
				.fetch();
	}
	

	@PersistenceUnit
	EntityManagerFactory emf;
	
	@Test
	public void fetchJoinNo() {
		em.flush();
		em.clear();
		
		Member findMember = queryFactory
								.selectFrom(member)
								.where(member.username.eq("member1"))
								.fetchOne();
		
		boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
	}
	
	@Test
	public void fetchJoinUse() {
		em.flush();
		em.clear();
		
		Member findMember = queryFactory
								.selectFrom(member)
								.join(member.team, team).fetchJoin()
								.where(member.username.eq("member1"))
								.fetchOne();
		
		boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
	}
	
	/*
	 * ���̰� ���� ���� ȸ�� ��ȸ
	 */
	@Test
	public void subQuery() {
		
		QMember memberSub = new QMember("memberSub");
		
		List<Member> result = queryFactory
								.selectFrom(member)
								.where(member.age.eq(
										JPAExpressions
											.select(memberSub.age.max())
											.from(memberSub)
								))
								.fetch();
		
		Assertions.assertThat(result).extracting("age")
									.containsExactly(40);
	}
	
	
	/*
	 * ���̰� ��� �̻��� ȸ�� ��ȸ
	 */
	@Test
	public void subQueryGoe() {
		
		QMember memberSub = new QMember("memberSub");
		
		List<Member> result = queryFactory
								.selectFrom(member)
								.where(member.age.goe(
										JPAExpressions
											.select(memberSub.age.avg())
											.from(memberSub)
								))
								.fetch();
		
		Assertions.assertThat(result).extracting("age")
									.containsExactly(30,40);
	}
	
	
	/*
	 * in �� ����
	 */
	@Test
	public void subQueryIn() {
		
		QMember memberSub = new QMember("memberSub");
		
		List<Member> result = queryFactory
								.selectFrom(member)
								.where(member.age.in(
										JPAExpressions
											.select(memberSub.age)
											.from(memberSub)
											.where(memberSub.age.gt(10))
								))
								.fetch();
		
		Assertions.assertThat(result).extracting("age")
									.containsExactly(30,40);
	}
	
	
	@Test
	public void selectSubquery() {
		
		QMember memberSub = new QMember("memberSub");
		List<Tuple> result = queryFactory
				.select(member.username,
						JPAExpressions
						.select(memberSub.age.avg())
						.from(memberSub))
				.from(member)
				.fetch();
	}
	
	
	@Test
	public void basicCase() {
		List<String> result = queryFactory
			.select(member.age
					.when(10).then("����")
					.when(20).then("������")
					.otherwise("��Ÿ"))
			.from(member)
			.fetch();
	}
	
	
	@Test
	public void complexCase() {
		List<String> result = queryFactory
				.select(new CaseBuilder()
						.when(member.age.between(0, 20)).then("0~20��")
						.when(member.age.between(21, 30)).then("21~30��")
						.otherwise("��Ÿ"))
				.from(member)
				.fetch();
	}
	
	@Test
	public void constant() {
		List<Tuple> result = queryFactory
				.select(member.username, Expressions.constant("A"))
				.from(member)
				.fetch();
	}
	
	@Test
	public void concat() {
		// username_age
		List<String> result = queryFactory
				.select(member.username.concat("_").concat(member.age.stringValue()))
				.from(member)
				.where(member.username.eq("member1"))
				.fetch();
	}
	
	@Test
	public void simpleProjection(){
		List<String> result = queryFactory
				.select(member.username)
				.from(member)
				.fetch();
	}
	
	@Test
	public void tupleProjection() {
		List<Tuple> result = queryFactory
					.select(member.username, member.age)
					.from(member)
					.fetch();
		
		for(Tuple tuple : result) {
			tuple.get(member.username);
		}
	}
	
	
	@Test
	public void findDtoByJPQL() {
		List<MemberDto> result =  em.createQuery("select new study.querydsl.dto.MemberDto(m.usernname, m.age) from Member m",MemberDto.class)
										.getResultList();
	}
	
	// setter�� ���ؼ� ���� ��
	@Test
	public void findDtoBySetter() {
		List<MemberDto> result = queryFactory
				.select(Projections.bean(MemberDto.class,
						member.username,
						member.age))
				.from(member)
				.fetch();
	}
	
	
	// getter,setter ���� field �� ���� �ٷ� �������
	@Test
	public void findDtoByField() {
		List<MemberDto> result = queryFactory
				.select(Projections.fields(MemberDto.class,
						member.username,
						member.age))
				.from(member)
				.fetch();
	}
	
	// �����ڸ� ���ؼ� ���� ����־����
	@Test
	public void findDtoByConstructor() {
		List<MemberDto> result = queryFactory
				.select(Projections.constructor(MemberDto.class,
						member.username,
						member.age))
				.from(member)
				.fetch();
		
	}
	
	
	// ���� ���̸� �ִ� ���̸� �ް� �ʹٸ� ���������� ���� ��
	// ExpressionUtils�� �̿��ؼ� max age�� ����
	@Test
	public void findUserDto() {
		QMember memberSub = new QMember("memberSub");
		List<UserDto> result = queryFactory
				.select(Projections.fields(UserDto.class,
						member.username.as("name"),
						ExpressionUtils.as(JPAExpressions
								.select(memberSub.age.max())
									.from(memberSub), "age")
				))
				.from(member)
				.fetch();
	}
	
	
	// �����ڸ� ���ؼ� ���� ����־����
	@Test
	public void findUserDtoByConstructor() {
		List<UserDto> result = queryFactory
				.select(Projections.constructor(UserDto.class,
						member.username,
						member.age))
				.from(member)
				.fetch();

	}
	
	@Test
	public void findDtoByQueryProjection() {
		List<MemberDto> result = queryFactory
				.select(new QMemberDto(member.username, member.age))
				.from(member)
				.fetch();
	}
	
	
	@Test
	public void dynamicQuery_BooleanBuilder() {
		String usernameParam = "member1";
		Integer ageParam = 10;
		
		List<Member> result = searchMember1(usernameParam, ageParam);
	}

	private List<Member> searchMember1(String usernameCond, Integer ageCond) {
		BooleanBuilder builder = new BooleanBuilder();
		if(usernameCond != null) {
			builder.and(member.username.eq(usernameCond));
		}
		if(ageCond != null) {
			builder.and(member.age.eq(ageCond));
		}
		return queryFactory
				.selectFrom(member)
				.where(builder)
				.fetch();
	}
	
	
	@Test
	public void dynamicQuery_WhereParam() {
		String usernameParam = "member1";
		Integer ageParam = 10;
		
		List<Member> result = searchMember2(usernameParam, ageParam);
	}

	private List<Member> searchMember2(String usernameCond, Integer ageCond) {
		return queryFactory
						.selectFrom(member)
						.where(usernameEq(usernameCond), ageEq(ageCond))
//						.where(allEq(usernameCond, ageCond))
						.fetch();
	}
	
	private BooleanExpression usernameEq(String usernameCond) {
		return usernameCond == null ? null : member.username.eq(usernameCond);
	}

	private BooleanExpression ageEq(Integer ageCond) {
		return ageCond == null ? null : member.age.eq(ageCond);
	}

	private BooleanExpression allEq(String usernameCond, Integer ageCond) {
		return usernameEq(usernameCond).and(ageEq(ageCond));
	}
	
	
	@Test
	@Commit
	public void bulkUpdate() {
		
		// member1 = 10 -> ��ȸ��
		// member2 = 20 -> ��ȸ��
		// member3 = 30 -> ȸ��
		// member4 = 40 -> ȸ��
		
		long count = queryFactory	
				.update(member)
				.set(member.username, "��ȸ��")
				.where(member.age.lt(28))
				.execute();
	}
	
	
	@Test
	public void bulkAdd() {
		long count = queryFactory
							.update(member)
							.set(member.age, member.age.add(1))
							.execute();
	}
	
	@Test
	public void bulkDelete() {
		long count = queryFactory
					.delete(member)
					.where(member.age.gt(18))
					.execute();
	}
	
	
	@Test
	public void sqlFunction() {
		List<String> result = queryFactory
				.select(Expressions.stringTemplate(
						"function('replace',{0},{1},{2})", 
						member.username,"member","M"))
				.from(member)
				.fetch();
	}
	
	
	@Test
	public void sqlFunction2() {
		List<String> result = queryFactory
				.select(member.username)
				.from(member)
//				.where(member.username.eq(
//						Expressions.stringTemplate(
//								"function('lower',{0})", 
//								member.username)))
				.where(member.username.eq(member.username.lower()))
				.fetch();
	}
}