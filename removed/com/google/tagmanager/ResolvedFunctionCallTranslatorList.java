package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;
import java.util.List;

interface ResolvedFunctionCallTranslatorList {
    void translateAndAddAll(List<ResourceUtil.ExpandedFunctionCall> list, List<String> list2);
}
