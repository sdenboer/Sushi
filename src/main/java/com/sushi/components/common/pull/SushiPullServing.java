package com.sushi.components.common.pull;

import com.sushi.components.common.serving.SushiServing;
import com.sushi.components.common.serving.SushiServingStatus;
import com.sushi.components.common.serving.SushiServingWrapper;
import java.util.Collections;

import java.util.Set;

public class SushiPullServing extends SushiServing {
    public SushiPullServing(SushiServingStatus sushiServingStatus) {
        super(sushiServingStatus);
    }

    @Override
    public Set<SushiServingWrapper> optionalSushiWrappers() {
        return Collections.emptySet();
    }
}
