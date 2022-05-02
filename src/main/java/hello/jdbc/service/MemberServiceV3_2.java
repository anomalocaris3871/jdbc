
package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        txTemplate.executeWithoutResult((status) -> {
            Member fromMember = null;
            try {
                fromMember = memberRepository.findById(fromId);
                Member toMember = memberRepository.findById(toId);

                memberRepository.update(fromId, fromMember.getMoney() - money);

                if (toMember.getMemberId().equals("memberEX")) {
                    throw new IllegalStateException("transfer exception occurs");
                }

                memberRepository.update(toId, toMember.getMoney() + money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
