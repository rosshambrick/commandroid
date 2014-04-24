//package com.rosshambrick.covert.tests.commands;
//
//import com.rosshambrick.covert.CommandListener;
//import com.rosshambrick.covert.CovertAgent;
//import com.rosshambrick.covert.tests.mocks.MockCommand;
//import com.rosshambrick.covert.tests.mocks.MockExecutor;
//import org.junit.Before;
//import org.junit.Test;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNull;
//
//public class WhenSendingLotsOfCommandsWithNoListeners implements CommandListener<MockCommand> {
//    private MockExecutor mExecutor;
//    private MockCommand mFailedCommand;
//    private MockCommand mCompleteCommand;
//
//    @Before
//    public void setup() {
//        mExecutor = new MockExecutor();
//        CovertAgent commandProcessor = new CovertAgent(
//                null,
//                mExecutor,
//                null);
//
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//        commandProcessor.send(new MockCommand());
//
//        commandProcessor.resetListener(id1, this);
//    }
//
//    @Override
//    public void commandComplete(MockCommand command) {
//        if (command.isSuccess()) {
//            mCompleteCommand = command;
////        } else {
////            mFailedCommand = command;
//        }
//    }
//
////    @Override
////    public void commandFailed(MockCommand command) {
////        mFailedCommand = command;
////    }
//
//    @Test
//    public void shouldSendElevenCommands() {
//        assertEquals(11, mExecutor.getMessagesSentCount());
//    }
//
//    @Test
//    public void shouldNoLongerBeAbleToRetryFirstCommand() {
//        assertNull(mCompleteCommand);
//        assertNull(mFailedCommand);
//    }
//}