PropertiesAPI.getProperty("kir", "ab", file).thenAccept((x) -> {
			Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
				if(x != "ab")
					//do something
				else
					// do something
			});
		});
