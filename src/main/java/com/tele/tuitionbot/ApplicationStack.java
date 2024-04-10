package com.tele.tuitionbot;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

public class ApplicationStack extends Stack {
    public ApplicationStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public ApplicationStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // define stack here
        Bucket.Builder.create(this, "MyFirstBucket")
                .versioned(true).build();
    }
}
