package github.gvnc.camunda.starter.config;

import github.gvnc.camunda.starter.executionListener.SequenceFlowExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomBpmnParseListener extends AbstractBpmnParseListener {

    @Autowired
    private SequenceFlowExecutionListener sequenceFlowExecutionListener;

    private boolean isActivityOfTypeUserTask (ActivityImpl activity){
        return activity != null && activity.getProperty("type") != null &&
                activity.getProperty("type").toString().equals("userTask");
    }

    @Override
    public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
        try {
            // check if sequence source is usertask
            ActivityImpl sourceActivity = transition.getSource();
            if (isActivityOfTypeUserTask(sourceActivity)) {
                String sequenceName = sequenceFlowElement.attribute("name");
                if (sequenceName != null && sequenceName.startsWith("UserChoice")) {
                    // do nothing
                } else{
                    log.error("Validation fails for sequence [{}], name does not start with UserChoice", sequenceName);
                }
            }

            // add sequence execution listener to all sequences which has a name
            if (sequenceFlowElement.attribute("name") != null) {
                log.trace("Adding execution listener to sequence flow", transition.getId());
                transition.addListener("take", sequenceFlowExecutionListener);
            }
        } catch (Exception e) {
            log.error("Failed parseSequenceFlow.", e);
        }
    }
}
