/*
 * GameStateData.java
 *
 * Generated by Protege plugin Beanynizer. 
 * Changes will be lost! 
 */
package jadex.examples.blackjack;

import java.util.ArrayList;
import java.util.List;


/**
 *  Java class for concept GameState of blackjack_beans ontology.
 */
public abstract class GameStateData implements java.beans.BeanInfo 
{
	//-------- constants ----------

	//-------- attributes ----------

	/** Attribute for slot dealer. */
	protected  Player  dealer;

	/** Attribute for slot players. */
	protected  List  players;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>GameState</code>.
	 */
	public GameStateData()  {
		this.players  = new ArrayList();
	}

	//-------- accessor methods --------

	/**
	 *  Get the dealer of this GameState.
	 * @return dealer
	 */
	public Player  getDealer() {
		return this.dealer;
	}

	/**
	 *  Set the dealer of this GameState.
	 * @param dealer the value to be set
	 */
	public void  setDealer(Player dealer) {
		this.dealer = dealer;
	}

	/**
	 *  Get the players of this GameState.
	 * @return players
	 */
	public Player[]  getPlayers() {
		return (Player[])players.toArray(new Player [players.size()]);
	}

	/**
	 *  Set the players of this GameState.
	 * @param players the value to be set
	 */
	public void  setPlayers(Player[] players) {
		this.players.clear();
		for(int i=0; i<players.length; i++)
			this.players.add(players[i]);
	}

	/**
	 *  Get an players of this GameState.
	 *  @param idx The index.
	 *  @return players
	 */
	public Player  getPlayer(int idx) {
		return (Player)this.players.get(idx);
	}

	/**
	 *  Set a player to this GameState.
	 *  @param idx The index.
	 *  @param player a value to be added
	 */
	public void  setPlayer(int idx, Player player) {
		this.players.set(idx, player);
	}

	/**
	 *  Add a player to this GameState.
	 *  @param player a value to be removed
	 */
	public void  addPlayer(Player player)  {
		this.players.add(player);
	}

	/**
	 *  Remove a player from this GameState.
	 *  @param player a value to be removed
	 *  @return  True when the players have changed.
	 */
	public boolean  removePlayer(Player player)  {
		return this.players.remove(player);
	}


	//-------- bean related methods --------

	/** The property descriptors, constructed on first access. */
	private java.beans.PropertyDescriptor[] pds = null;

	/**
	 *  Get the bean descriptor.
	 *  @return The bean descriptor.
	 */
	public java.beans.BeanDescriptor getBeanDescriptor() {
		return null;
	}

	/**
	 *  Get the property descriptors.
	 *  @return The property descriptors.
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		if(pds==null) {
			try {
				pds = new java.beans.PropertyDescriptor[]{
					 new java.beans.PropertyDescriptor("dealer", this.getClass(), "getDealer", "setDealer")
					, new java.beans.IndexedPropertyDescriptor("players", this.getClass(),
						"getPlayers", "setPlayers", "getPlayer", "setPlayer")
				};
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return pds;
	}

	/**
	 *  Get the default property index.
	 *  @return The property index.
	 */
	public int getDefaultPropertyIndex() {
		return -1;
	}

	/**
	 *  Get the event set descriptors.
	 *  @return The event set descriptors.
	 */
	public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
		return null;
	}

	/**
	 *  Get the default event index.
	 *  @return The default event index.
	 */
	public int getDefaultEventIndex() {
		return -1;
	}

	/**
	 *  Get the method descriptors.
	 *  @return The method descriptors.
	 */
	public java.beans.MethodDescriptor[] getMethodDescriptors() {
		return null;
	}

	/**
	 *  Get additional bean info.
	 *  @return Get additional bean info.
	 */
	public java.beans.BeanInfo[] getAdditionalBeanInfo() {
		return null;
	}

	/**
	 *  Get the icon.
	 *  @return The icon.
	 */
	public java.awt.Image getIcon(int iconKind) {
		return null;
	}

	/**
	 *  Load the image.
	 *  @return The image.
	 */
	public java.awt.Image loadImage(final String resourceName) {
		try {
			final Class c = getClass();
			java.awt.image.ImageProducer ip = (java.awt.image.ImageProducer)
				java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {
					public Object run(){
						java.net.URL url;
						if((url = c.getResource(resourceName))==null) {
							return null;
						}
						else {
							try {
								return url.getContent();
							}
							catch(java.io.IOException ioe) {
								return null;
							}
						}
					}
				});
			if(ip==null)
				return null;
			java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
			return tk.createImage(ip);
		}
		catch(Exception ex) {
			return null;
		}
	}

	//-------- additional methods --------

	/**
	 *  Get a string representation of this GameState.
	 *  @return The string representation.
	 */
	public String toString() {
		return "GameState("
           + ")";
	}

}
