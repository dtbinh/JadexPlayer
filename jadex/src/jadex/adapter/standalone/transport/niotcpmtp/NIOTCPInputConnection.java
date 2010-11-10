package jadex.adapter.standalone.transport.niotcpmtp;

import jadex.adapter.standalone.IMessageEnvelope;
import jadex.adapter.standalone.transport.codecs.CodecFactory;
import jadex.adapter.standalone.transport.codecs.IDecoder;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 *  The input connection (channel) for incoming requests. 
 */
public class NIOTCPInputConnection
{
	//-------- constants ---------
	
	/** 2MB as message buffer */
	static final int BUFFER_SIZE = 1024 * 1024 * 2;
   
	//-------- attributes ---------

	/** The socket channel for receiving messages. */
	protected SocketChannel sc;

	/** The write buffer for the channel. */
	protected ByteBuffer writebuffer;

	/** The read buffer for reading out the messages. */
	protected ByteBuffer readbuffer;
   
	/** The current message length (-1 for none). */
	protected int msg_end;
	
	/** The codec id. */
	protected byte codec_id;

	/** The codec factory. */
	protected CodecFactory codecfac;
	
	//-------- constrcutors --------
	
	/** 
	 * Constructor for InputConnection.
	 * @param sc
	 * @param dec
	 * @throws IOException
	 */
	public NIOTCPInputConnection(SocketChannel sc, CodecFactory codecfac)
	{
		//System.out.println("Creating input con: "+sc);
		this.sc = sc;
		this.codecfac = codecfac;
		this.writebuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		this.readbuffer = writebuffer.asReadOnlyBuffer();
		msg_end = -1; // No message available.
		codec_id = -1; // No codec
	}

	//-------- methods --------

	/** 
	 *  Read a message from the channel.
	 *  @return The message if a complete message is finished.
	 *  @throws Exception on read error.
	 */
   public IMessageEnvelope read() throws IOException
   {
	   IMessageEnvelope ret = null;
	   
	   // Write data from channel into the buffer.
	   if(sc.read(writebuffer)==-1)
		   throw new IOException("Channel closed.");

	   // First try to determine the message size if unknown (-1)
	   // Read next msg header
	   // Need at least 4 size bytes
	   if(msg_end==-1 && writebuffer.position() >= NIOTCPTransport.PROLOG_SIZE)
	   { 
		   codec_id = readbuffer.get();
			   
		   msg_end = readbuffer.getInt();
         
		   if(msg_end > writebuffer.limit())
			   throw new BufferOverflowException();
		   if(msg_end <= 0) 
			   throw new BufferUnderflowException();
	   }

	   // Read out the buffer if enougth data has been retrieved for the message.
	   if(msg_end!=-1 && msg_end <= writebuffer.position())
	   {
		   // Prepare reading out the buffer
		   readbuffer.limit(msg_end);
		   byte[] rawbytes = new byte[msg_end-NIOTCPTransport.PROLOG_SIZE];
		   readbuffer.get(rawbytes);
		   IDecoder dec = codecfac.getDecoder(codec_id);
		   ret = (IMessageEnvelope)dec.decode(rawbytes);
		   // Reset the readbuffer and compact (i.e. copy rest) the writebuffer
		   writebuffer.limit(writebuffer.position());
		   writebuffer.position(msg_end);
		   writebuffer.compact();
		   readbuffer.clear();
		   msg_end = -1;
		   codec_id = -1;
	   }

	   return ret;
   }
   
   /**
	*  Close the connection.
	*/
	public void close()
	{
		try
		{
			sc.close();
		}
		catch(IOException e)
		{
			//e.printStackTrace();
		}
	}
}