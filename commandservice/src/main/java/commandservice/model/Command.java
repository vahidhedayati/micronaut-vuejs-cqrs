package commandservice.model;

public interface Command<T> {
	
	public default String getCommandName() {
		System.out.println("getCommandName: " +  this.getClass().getSimpleName());
		return this.getClass().getSimpleName();
	}

}
