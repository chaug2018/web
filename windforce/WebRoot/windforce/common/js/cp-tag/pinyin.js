/**
 * 获得指定字符串匹配的汉字拼音组合
 * 
 * @param words	需要获得匹配数组的源字符串
 * @param supportInitial	是否需要支持首字母匹配
 * @returns
 */
window.getMatchPinyin = function(words,supportInitial){
	var pinYin = getPinyin(words);
	if(supportInitial){
		pinYin = pinYin.concat(getPinyinInitials(words));
	}
	return pinYin;
};
//获得词组的汉字与拼音全拼的随机组合集合
window.getPinyin = function(words){
	var result = [""];
	for(var i = 0; i < words.length; i++){
		//temp放入初始字符
		var temp = [words.charAt(i)];
		//字符与字符拼音数组合并为新数组
		temp = temp.concat(crossWalks["H"+words.charCodeAt(i).toString(16)]);
		var tempArr = [];
		for(var k = 0; k < temp.length; k++){
			for(var j = 0; j < result.length; j++){
				//保证一个option最多只有20条拼音全拼匹配，
				//避免因字段过长导致的js运算时间过长
				if(tempArr.length > 19){
					break;
				}
				tempArr.push(result[j] + temp[k]);
			}
		}
		result = tempArr;
	}
	return result;
};
//获得词组的拼音全拼的随机组合集合
window.getPinyin2 = function(words){
	var result = [""];
	for(var i = 0; i < words.length; i++){
		//取字符的拼音全拼数组，若非对照表内的汉字则取直接取该字符
		var temp = crossWalks["H"+words.charCodeAt(i).toString(16)] || words.charAt(i);
		var tempArr = [];
		if(typeof(temp) == "string"){
			for(var j = 0; j < result.length; j++){
				result[j] += temp;
			}
		}else{
			for(var k = 0; k < temp.length; k++){
				for(var j = 0; j < result.length; j++){
					//保证一个option最多只有20条拼音全拼匹配，
					//避免因字段过长导致的js运算时间过长
					if(tempArr.length > 19){
						break;
					}
					tempArr.push(result[j] + temp[k]);
				}
			}
			result = tempArr;
		}
	}
	return result;
};
//获得词组的拼音首字母的随机组合集合
window.getPinyinInitials = function(words){
	var result = [""];
	for(var i = 0; i < words.length; i++){
		//取字符的拼音全拼数组，若非对照表内的汉字则取直接取该字符
		var temp = crossWalks["H"+words.charCodeAt(i).toString(16)] || words.charAt(i);
		var tempArr = [];
		if(typeof(temp) == "string"){
			for(var j = 0; j < result.length; j++){
				result[j] += temp;
			}
		}else{
			for(var k = 0; k < temp.length; k++){
				for(var j = 0; j < result.length; j++){
					//保证一个option最多只有20条字母匹配，
					//避免因字段过长导致的js运算时间过长
					if(tempArr.length > 19){
						break;
					}
					tempArr.push(result[j] + temp[k].charAt(0));
				}
			}
			result = tempArr;
		}
	}
	return result;
};