package kiwi.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

class HttpServletResponseStub implements HttpServletResponse {
    public HttpServletResponseStub(PrintWriter writer) {
        this.writer = writer;
        this.status = 200;
        this.contentType = "text/html";
        this.characterEncoding = "iso-8859-1";
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }
    @Override
    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public String getContentType() {
        return contentType;
    }
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    @Override
    public void setStatus(int arg0, String arg1) {
        // deprecated
        assert(false);
    }

    PrintWriter writer;
    int status;
    String contentType;
    String characterEncoding;

    @Override
    public void flushBuffer() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getBufferSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isCommitted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetBuffer() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBufferSize(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContentLength(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLocale(Locale arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addCookie(Cookie arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean containsHeader(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String encodeRedirectURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeRedirectUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendError(int arg0) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendError(int arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendRedirect(String arg0) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub
        
    }
}
