package designMode.eventStream;

import java.awt.*;

public interface IEventHandlerContext  {
   EventHandlerContext fireEventRead();
   EventHandlerContext fireEventWrite();
   IEventHandler handler();
}
