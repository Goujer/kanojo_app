package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

abstract class CombiningEvaluator extends Evaluator {
    final List<Evaluator> evaluators;

    CombiningEvaluator() {
        this.evaluators = new ArrayList();
    }

    CombiningEvaluator(Collection<Evaluator> evaluators2) {
        this();
        this.evaluators.addAll(evaluators2);
    }

    /* access modifiers changed from: package-private */
    public Evaluator rightMostEvaluator() {
        if (this.evaluators.size() > 0) {
            return this.evaluators.get(this.evaluators.size() - 1);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void replaceRightMostEvaluator(Evaluator replacement) {
        this.evaluators.set(this.evaluators.size() - 1, replacement);
    }

    static final class And extends CombiningEvaluator {
        And(Collection<Evaluator> evaluators) {
            super(evaluators);
        }

        And(Evaluator... evaluators) {
            this((Collection<Evaluator>) Arrays.asList(evaluators));
        }

        public boolean matches(Element root, Element node) {
            for (int i = 0; i < this.evaluators.size(); i++) {
                if (!((Evaluator) this.evaluators.get(i)).matches(root, node)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return StringUtil.join((Collection) this.evaluators, " ");
        }
    }

    static final class Or extends CombiningEvaluator {
        Or(Collection<Evaluator> evaluators) {
            if (evaluators.size() > 1) {
                this.evaluators.add(new And(evaluators));
            } else {
                this.evaluators.addAll(evaluators);
            }
        }

        Or() {
        }

        public void add(Evaluator e) {
            this.evaluators.add(e);
        }

        public boolean matches(Element root, Element node) {
            for (int i = 0; i < this.evaluators.size(); i++) {
                if (((Evaluator) this.evaluators.get(i)).matches(root, node)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(":or%s", new Object[]{this.evaluators});
        }
    }
}
