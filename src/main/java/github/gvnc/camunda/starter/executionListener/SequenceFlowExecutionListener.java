package github.gvnc.camunda.starter.executionListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SequenceFlowExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        try {
            ExecutionEntity executionEntity = (ExecutionEntity) delegateExecution;
            String processKey = executionEntity.getProcessDefinition().getKey();
            String rootProcessInstanceId = executionEntity.getRootProcessInstanceId();
            String transitionName = executionEntity.getTransition().getProperty("name").toString();
            log.info("Sequence flow event [processKey={}, rootProcessInstanceId={}, transitionName={}]", processKey, rootProcessInstanceId, transitionName);

        } catch (Exception e) {
            log.error("Failed to execute notify.", e);
        }
    }
}
