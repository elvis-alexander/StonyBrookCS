# import time
# import math
# import sys
# sys.path.append('../utils')
# import searchDelegator as SD
#
#
# print "=================================================="
# print "TEST: RANKING BY TIME"
# print "=================================================="
#
# rank_algorithm = SD.rank_algorithm_time
# now = time.time()
# tweet_array_time = []
# print "now:", now,"\n"
#
#
# t,l,r = now-60, 4, 5
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 1,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
# t,l,r = now-120,  4, 5
# print "after 2 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 2,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
# t = now-180
# l = 4
# r = 5
# print "after 3 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 3,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
# t = now-240
# l = 4
# r = 5
# print "after 4 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 4,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-300
# l = 4
# r = 5
# print "after 5 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 5,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-360
# l = 4
# r = 5
# print "after 6 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 6,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# print "\n\n"
# print("doubling time")
# print "\n\n"
#
# t = now-720
# l = 4
# r = 5
# print "after 12 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 7,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-1440
# l = 4
# r = 5
# print "after 24 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 8,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-2880
# l = 4
# r = 5
# print "after 48 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 9,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-5760
# l = 4
# r = 5
# print "after 96 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 10,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
# print "=============================================="
# print "BOTH SETS OF IDS SHOULD INCREASE, SINCE TIME IS ONLY THING CHANGE"
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_time, True)
# for i in r:
#     print i["uid"]
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_time, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
#
#
# print "--------------------------------------------------------"
# print "successive posts"
# print "--------------------------------------------------------"
# tweet_array_time = []
# t = now-1
# l = 4
# r = 5
#
# print "after 1 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 11,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-2
# l = 4
# r = 5
# print "after 2 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 12,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-4
# l = 4
# r = 5
# print "after 4 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 13,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-8
# l = 4
# r = 5
# print "after 8 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 14,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-8
# l = 4
# r = 5
# print "after 8 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 15,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# t = now-16
# l = 4
# r = 5
# print "after 16 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 16,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
#
# print "\n\n"
# print("doubling time")
# print "\n\n"
#
# t = now-32
# l = 4
# r = 5
# print "after 32 sec"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 17,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
#
# t = now-64
# l = 4
# r = 5
# print "after 64 sec"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 18,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_time.append(td)
#
# print "=============================================="
# print "BOTH SETS OF IDS SHOULD INCREASE, SINCE TIME IS ONLY THING CHANGE"
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_time, True)
# for i in r:
#     print i["uid"]
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_time, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
#
#
# print "\n\n=================================================="
# print "TEST: RANKING BY INTEREST"
# print "=================================================="
#
#
#
# rank_algorithm = SD.rank_algorithm_interest
# tweet_array_interest = []
# now = time.time()
# print "now:", now,"\n"
#
#
# t = now-60
# l = 4
# r = 5
#
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 20,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
#
# t = now-120
# l = 4
# r = 5
# print "after 2 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 21,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-180
# l = 4
# r = 5
# print "after 3 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 22,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-240
# l = 4
# r = 5
# print "after 4 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 23,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-300
# l = 4
# r = 5
# print "after 5 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 24,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# t = now-360
# l = 4
# r = 5
# print "after 6 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 25,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# print "\n\n"
# print("doubling time")
# print "\n\n"
#
# t = now-720
# l = 4
# r = 5
# print "after 12 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 26,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-1440
# l = 4
# r = 5
# print "after 24 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 27,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-2880
# l = 4
# r = 5
# print "after 48 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 28,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-5760
# l = 4
# r = 5
# print "after 96 min"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 29,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# print "=============================================="
# print "BOTH SETS OF IDS SHOULD INCREASE, SINCE TIME IS ONLY THING CHANGE"
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_interest, True)
# for i in r:
#     print i["uid"]
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_interest, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
#
# print "--------------------------------------------------------"
# print "successive posts"
# print "--------------------------------------------------------"
#
# tweet_array_interest =[]
# t = now-1
# l = 4
# r = 5
#
# print "after 1 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 30,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-2
# l = 4
# r = 5
# print "after 2 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 31,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-4
# l = 4
# r = 5
# print "after 4 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 32,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
#
# t = now-8
# l = 4
# r = 5
# print "after 8 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 33,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# t = now-8
# l = 4
# r = 5
# print "after 8 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 34,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# t = now-16
# l = 4
# r = 5
# print "after 16 sec:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 35,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# print "\n\n"
# print("doubling time")
# print "\n\n"
#
# t = now-32
# l = 4
# r = 5
# print "after 32 sec"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 36,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
# t = now-64
# l = 4
# r = 5
# print "after 64 sec"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 37,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_interest.append(td)
#
#
# print "=============================================="
# print "BOTH SETS OF IDS SHOULD INCREASE, SINCE TIME IS ONLY THING CHANGE"
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_interest, True)
# for i in r:
#     print i["uid"]
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_interest, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
#
# print "\n\n=================================================="
# print "TEST: RANKING BY INTEREST => Changing INTEREST"
# print "=================================================="
# rank_algorithm = SD.rank_algorithm_interest
# tweet_array_change_interest = []
# now = time.time()
# print "now:", now,"\n"
#
#
# t = now-60
# l = 4
# r = 5
#
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 100,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
# t = now-60
# l = 8
# r = 10
#
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 101,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
# t = now-60
# l = 16
# r = 20
#
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 102,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
# t = now-60
# l = 40
# r = 50
#
# print "after 1 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 103,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
#
# print "=============================================="
# print "BOTH SETS OF IDS SHOULD DECREASE, SINCE INTEREST IS ONLY THING CHANGE. HIGHER INTEREST IN HIGHER ID'S"
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_change_interest, True)
# for i in r:
#     print i["uid"]
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_change_interest, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
#
# t = now-120
# l = 4
# r = 5
#
# print "after 2 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 104,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
# t = now-120
# l = 8
# r = 10
#
# print "after 2 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 105,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
# t = now-120
# l = 16
# r = 20
#
# print "after 2 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 106,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
# print "=============================================="
# print "IDs 100-103(1 MINUTE) SHOULD MAP TO 104-106(2 MINUTE)"
# print "DECREASING ID'S. HIGHER ID = HIGHER INTEREST"
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_change_interest, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
#
# t = now-1200
# l = 4
# r = 5
# print "after 20 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 200,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
#
# t = now-1200
# l = 8
# r = 10
# print "after 20 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 201,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
# t = now-1200
# l = 16
# r = 20
# print "after 20 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 202,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
#
# t = now-1200
# l = 32
# r = 40
# print "after 20 minute:"
# print t, l, r
# print rank_algorithm(t,l,r),"\n"
# td = {
#     "uid": 203,
#     "tweetstamp": t,
#     "likes": l,
#     "retweets": r
# }
# tweet_array_change_interest.append(td)
#
# print "=============================================="
# print "IDs 100-103(1 MINUTE) SHOULD MAP TO 104-106(2 MINUTE), 200-203 (20 MINUTE)"
# print "DECREASING ID'S. HIGHER ID = HIGHER INTEREST"
# print "=============================================="
# print "=============================================="
# r = SD.rank_result_tweets(tweet_array_change_interest, False)
# for i in r:
#     print i["uid"]
# print "=============================================="
