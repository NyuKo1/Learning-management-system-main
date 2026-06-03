package kz.sec.lms.shared.audit;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

public class CachedBodyServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public CachedBodyServletInputStream(byte[] body) {
        this.buffer = new ByteArrayInputStream(body);
    }

    @Override public int read() { return buffer.read(); }
    @Override public boolean isFinished() { return buffer.available() == 0; }
    @Override public boolean isReady() { return true; }
    @Override public void setReadListener(ReadListener readListener) {}
}
